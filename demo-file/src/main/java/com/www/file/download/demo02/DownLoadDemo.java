package com.www.file.download.demo02;

import com.mysql.cj.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DownLoadDemo {
    private static final long blockSize = 1024 * 1024 * 2; // 每个线程下载 1MB 1024 *1024
    private final URI uri;
    private final String localPath;
    private final ConcurrentHashMap<Long, DownloadThread> aliveThread = new ConcurrentHashMap<>();
    private CountDownLatch latch;
    private final AtomicBoolean status = new AtomicBoolean(true);
    private volatile boolean isPaused = false;
    private volatile boolean isCancelled = false;
    private final ConcurrentHashMap<Long, Long> progressMap = new ConcurrentHashMap<>(); // 记录每个线程进度

    public DownLoadDemo(URI uri, String localPath) {
        this.uri = uri;
        this.localPath = localPath;
    }

    // 取消下载
    public void cancel() {
        isCancelled = true;
        for (DownloadThread thread : aliveThread.values()) {
            thread.abort();
        }
        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }
    }

    // 暂停下载
    public void pause() {
        isPaused = true;
        for (DownloadThread thread : aliveThread.values()) {
            thread.pause();
        }
    }

    // 恢复下载
    public void resume() {
        if (isPaused && !isCancelled) {
            isPaused = false;
            long fileSize = getFileSize();
            if (fileSize <= 0) {
                log.error("Cannot resume: invalid file size {}", fileSize);
                return;
            }
            int threadCount = progressMap.size();
            latch = new CountDownLatch(threadCount);
            for (long threadId : progressMap.keySet()) {
                long completed = progressMap.getOrDefault(threadId, 0L);
                long startIndex = (threadId - 1L) * blockSize;
                long endIndex = Math.min(startIndex + blockSize - 1, fileSize - 1);

                DownloadThread downloadThread = new DownloadThread(threadId, startIndex, endIndex, completed);
                aliveThread.put(threadId, downloadThread);
                downloadThread.start();
            }
            try {
                latch.await(); // 等待所有线程完成
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Resume interrupted", e);
            }
        }
    }

    // 重新开始下载
    public void restart() {
        cancel();
        progressMap.clear();
        isCancelled = false;
        isPaused = false;
        start();
    }

    // 获取文件大小
    private long getFileSize() {
        int retries = 3;
        for (int i = 0; i < retries; i++) {
            try {
                URL url = uri.toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(RequestMethod.GET.name());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                int code = connection.getResponseCode();
                long size = 0;
                if (HttpStatus.SC_OK == code) {
                    size = connection.getContentLength();
                }
                connection.disconnect();
                return size;
            } catch (IOException e) {
                log.error("Failed to get file size, retry {}/{}", i + 1, retries, e);
                if (i < retries - 1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return -1;
    }

    // 开始下载
    public boolean start() {
        try {
            long length = getFileSize();
            if (length < 0) return false;

            RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
            raf.setLength(length);
            raf.close();

            int count = (int) Math.ceil((double) length / blockSize);
            latch = new CountDownLatch(count);
            log.info("MultiThreadDownLoad start file size {} Thread number {}", length, count);

            for (long threadId = 1L; threadId <= count; ++threadId) {
                long startIndex = (threadId - 1L) * blockSize;
                long endIndex = Math.min(startIndex + blockSize - 1, length - 1);

                DownloadThread downloadThread = new DownloadThread(threadId, startIndex, endIndex, 0);
                aliveThread.put(threadId, downloadThread);
                downloadThread.start();
            }
            Thread.sleep(3000);
            latch.await();
        } catch (Exception e) {
            status.set(false);
            log.error("Download failed", e);
        }

        if (status.get() && !isCancelled) {
            log.info("MultiThreadDownLoad success");
        } else {
            File file = new File(localPath);
            if (file.exists()) {
                file.delete();
            }
            log.error("MultiThreadDownLoad fail");
        }
        return status.get() && !isCancelled;
    }

    // 创建配置好的 HTTP 客户端
    private CloseableHttpClient createHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000) // 连接超时 5 秒
                .setConnectionRequestTimeout(5000) // 请求超时 5 秒
                .setSocketTimeout(10000) // 读取超时 10 秒
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setRetryHandler((exception, executionCount, context) -> executionCount < 3) // 自动重试 3 次
                .build();
    }

    // 下载线程
    public class DownloadThread extends Thread {
        private final long threadId;
        private final long startIndex;
        private final long endIndex;
        private long completed;
        private volatile boolean threadPaused = false;
        private volatile HttpGet get; // 保存 HttpGet 实例

        public DownloadThread(long threadId, long startIndex, long endIndex, long completed) {
            if (endIndex < startIndex) {
                throw new IllegalArgumentException("endIndex (" + endIndex + ") must be >= startIndex (" + startIndex + ")");
            }
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.completed = completed;
        }

        public void abort() {
            log.info("Forcibly stop thread-{}", threadId);
            if (get != null) {
                get.abort(); // 中止请求
            }
            interrupt();
        }

        public void pause() {
            if (get != null) {
                get.abort(); // 中止请求
            }
            threadPaused = true;
        }

        @Override
        public void run() {
            int maxRetries = 3;
            int retryCount = 0;

            while (retryCount < maxRetries && !isCancelled && !threadPaused) {
                InputStream stream = null;
                RandomAccessFile raf = null;
                CloseableHttpClient client = null;

                try {
                    client = createHttpClient();
                     get = new HttpGet(uri);
                    log.info("Thread-{} Downloading bytes [{},{}]", threadId, startIndex + completed, endIndex);
                    Header header = new BasicHeader("Range", "bytes=" + (startIndex + completed) + "-" + endIndex);
                    get.setHeader(header);

                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    stream = entity.getContent();
                    raf = new RandomAccessFile(localPath, "rwd");
                    raf.seek(startIndex + completed);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len =stream.read(buffer)) != -1 && !isCancelled && !threadPaused) {
                        raf.write(buffer, 0, len);
                        completed += len;
                        progressMap.put(threadId, completed);
                    }

                    if (!isCancelled && !threadPaused) {
                        long actualEnd = startIndex + completed - 1;
                        log.info("Thread-{} Download completed bytes [{},{}]", threadId, startIndex, actualEnd);
                    }
                    break;
                } catch (Exception e) {
                    status.set(false);
                    log.error("Thread-{} Download error: {}", threadId, e.getMessage());
                    retryCount++;
                    if (retryCount < maxRetries && !isCancelled && !threadPaused) {
                        try {
                            Thread.sleep(2000 * retryCount);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } finally {
                    try {
                        if (raf != null) raf.close();
                        if (stream != null) {
                            stream.close();
                            log.debug("Thread-{} InputStream closed", threadId);
                        }
                        if (client != null) {
                            client.close();
                            log.debug("Thread-{} HttpClient closed", threadId);
                        }
                    } catch (IOException e) {
                        log.error("Thread-{} Cleanup error", threadId, e);
                    }
                    if (!threadPaused && (retryCount >= maxRetries || !isCancelled)) {
                        aliveThread.remove(threadId);
                        latch.countDown();
                    }
                }
            }
        }
    }

}