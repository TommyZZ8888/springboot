package com.www.file.download.demo02;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MultiThreadDownLoadDemo02 {


    private static final long blockSize = 1024 * 1024 * 5;

    private final URI uri;

    private final ConcurrentHashMap<Long, DownloadThread> aliveThread = new ConcurrentHashMap<>();

    private final String localPath;

    private CountDownLatch latch;

    private final AtomicBoolean status = new AtomicBoolean(true);

    public MultiThreadDownLoadDemo02(URI uri, String localPath) {
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

    public boolean start() {
        try {
            long length = 0;
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(RequestMethod.GET.name());
            int code = connection.getResponseCode();
            if (HttpStatus.SC_OK == code) {
                length = connection.getContentLength();
            }

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
            this.get = new HttpGet(MultiThreadDownLoadDemo02.this.uri);
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
                    raf = new RandomAccessFile(MultiThreadDownLoadDemo02.this.localPath, "rwd");
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
                MultiThreadDownLoadDemo02.this.status.set(false);
                MultiThreadDownLoadDemo02.this.exit();
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
}
