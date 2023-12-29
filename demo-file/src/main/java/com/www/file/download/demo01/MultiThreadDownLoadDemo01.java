package com.www.file.download.demo01;

import com.www.file.download.MultiThreadDownLoad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * @Description DownLoadDemo01
 * @Author 张卫刚
 * @Date Created on 2023/12/29
 */
@Slf4j
public class MultiThreadDownLoadDemo01 {

    /**
     * 同时下载的线程数
     */
    private int threadCount;
    /**
     * 服务器请求路径
     */
    private String serverPath;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 线程计数同步辅助
     */
    private CountDownLatch latch;


    public MultiThreadDownLoadDemo01(int threadCount, String serverPath, String localPath, CountDownLatch latch) {
        this.threadCount = threadCount;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.latch = latch;
    }

    public void executeDownLoad() {
        try {
            URL url = URI.create(serverPath).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(RequestMethod.GET.name());
            int code = connection.getResponseCode();
            if (200 == code) {
                //服务器返回的数据长度，实际上就是文件的长度
                int length = connection.getContentLength();
                log.info("文件总长度：{}", length);
                RandomAccessFile randomAccessFile = new RandomAccessFile(localPath, "rwd");
                randomAccessFile.setLength(length);
                randomAccessFile.close();
                //分割文件
                int blockSize = length / threadCount;
                for (int threadId = 1; threadId <= threadCount; threadId++) {
                    int startIndex = (threadId - 1) * blockSize;
                    int endIndex = startIndex + blockSize - 1;
                    if (threadId == threadCount) {
                        endIndex = length;
                    }
                    log.info("线程{}下载:{}字节~{}字节", threadId, startIndex, endIndex);
                    new DownLoadThreadDemo01(threadId, startIndex, endIndex).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部类用于实现下载
     */
     class DownLoadThreadDemo01 extends Thread {
        /**
         * 线程id
         */
        private int threadId;
        /**
         * 下载起始位置
         */
        private int startIndex;
        /**
         * 下载结束位置
         */
        private int endIndex;

        public DownLoadThreadDemo01(int threadId, int startIndex, int endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }


        @Override
        public void run() {
            try {
                log.info("线程{}正在下载", threadId);
                URL url = URI.create(serverPath).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(RequestMethod.GET.name());
                //请求服务器下载部分文件的指定位置
                connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                connection.setConnectTimeout(5000);
                int code = connection.getResponseCode();
                log.info("线程{}请求返回code={}", threadId, code);

                InputStream inputStream = connection.getInputStream();
                RandomAccessFile randomAccessFile = new RandomAccessFile(localPath, "rwd");
                //随机写文件的时候从哪个位置开始写
                randomAccessFile.seek(startIndex);//定位文件

                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, len);
                }
                randomAccessFile.close();
                inputStream.close();
                log.info("线程{}下载完毕", threadId);
                //线程计数器-1
                latch.countDown();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

