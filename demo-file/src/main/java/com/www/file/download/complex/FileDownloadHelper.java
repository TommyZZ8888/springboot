package com.www.file.download.complex;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description FileDownLoadHelper
 * @Author 张卫刚
 * @Date Created on 2023/12/29
 */
@Slf4j
public class FileDownloadHelper {
    private static final long blockSize = 1024 * 1024;

    private final URI uri;

    private final ConcurrentHashMap<Long, FileDownloadHelper.DownloadThread> aliveThread = new ConcurrentHashMap<>();

    private final String localPath;

    private CountDownLatch latch;

    private final AtomicBoolean status = new AtomicBoolean(true);

    public FileDownloadHelper(URI uri, String localPath) {
        this.uri = uri;
        this.localPath = localPath;
    }

    private void exit() {
        for (Long threadId : this.aliveThread.keySet()) {
            if (this.aliveThread.containsKey(threadId)) {
                (this.aliveThread.get(threadId)).abort();
            }
        }
    }

    public boolean download() {
        try {
            long length = getLength();
            RandomAccessFile raf = new RandomAccessFile(this.localPath, "rwd");
            raf.setLength(length);
            raf.close();
            int count = (int) Math.ceil((double) length / blockSize);
            this.latch = new CountDownLatch(count);
            log.info("MultiThreadDownLoad start file size " + length + " Thread number " + count);

            for (long threadId = 1L; threadId <= (long) count; ++threadId) {
                long startIndex = (threadId - 1L) * blockSize;
                long endIndex = startIndex + blockSize - 1L;
                if (threadId == (long) count) {
                    endIndex = length;
                }

                log.info("Thread-" + threadId + " Prepare to download bytes [" + startIndex + "," + endIndex + "]");
                DownloadThread downloadThread = new DownloadThread(threadId, startIndex, endIndex);
                this.aliveThread.put(threadId, downloadThread);
                downloadThread.start();
            }

            this.latch.await();
        } catch (Exception var10) {
            this.status.set(false);
            var10.printStackTrace();
        }

        if (this.status.get()) {
            log.info("MultiThreadDownLoad success");
        } else {
            File file = new File(this.localPath);
            file.delete();
            log.error("MultiThreadDownLoad fail");
        }

        return this.status.get();
    }

    public class DownloadThread extends Thread {
        private final long threadId;
        private final long startIndex;
        private final long endIndex;
        private final HttpGet get;

        public DownloadThread(long threadId, long startIndex, long endIndex) {
            this.get = new HttpGet(FileDownloadHelper.this.uri);
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public void abort() {
            log.info("Forcibly stop thread-" + this.threadId);
            this.get.abort();
        }

        public void run() {
            InputStream stream = null;
            RandomAccessFile raf = null;
            try {
                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    log.info("Thread-" + this.threadId + " Downloading bytes [" + this.startIndex + "," + this.endIndex + "]");
                    Header header = new BasicHeader("Range", "bytes=" + this.startIndex + "-" + this.endIndex);
                    this.get.setHeader(header);
                    HttpResponse response = client.execute(this.get);
                    HttpEntity entity = response.getEntity();
                    stream = entity.getContent();
                    raf = new RandomAccessFile(FileDownloadHelper.this.localPath, "rwd");
                    raf.seek(this.startIndex);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = stream.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                    }
                    log.info("Thread-" + this.threadId + " Download completed bytes [" + this.startIndex + "," + this.endIndex + "]");
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileDownloadHelper.this.status.set(false);
                FileDownloadHelper.this.exit();
                log.error("Thread-" + this.threadId + " Download error bytes [" + this.startIndex + "," + this.endIndex + "] " + e.getMessage());
            } finally {
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }
            aliveThread.remove(this.threadId);
        }
    }

    private long getLength() {
        long length=0;
        try {
            HttpURLConnection connection = getConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                 length = connection.getContentLength();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    private HttpURLConnection getConnection() {
        HttpURLConnection connection = null;
        try {
            URL url = uri.toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(RequestMethod.GET.name());
            return connection;
        } catch (
                Exception e) {
            log.error("获取连接时发生异常：{}", e.getMessage());
        }
        return connection;
    }

}


