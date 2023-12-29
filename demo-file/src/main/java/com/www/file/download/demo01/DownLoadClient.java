package com.www.file.download.demo01;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @Description DownLoadService
 * @Author 张卫刚
 * @Date Created on 2023/12/28
 */
@Slf4j
public class DownLoadClient {
    public static void main(String[] args) {
        int threadSize = 2;
        String serverPath = "https://pics1.baidu.com/feed/8644ebf81a4c510ff3d1252bb6f37e24d42aa51a.png";
        String localPath = "demo-file/src/main/resources/static/stefanie.png";
        CountDownLatch latch = new CountDownLatch(threadSize);
        MultiThreadDownLoadDemo01 downLoad = new MultiThreadDownLoadDemo01(threadSize, serverPath, localPath, latch);
        long startTime = System.currentTimeMillis();
        try {
            downLoad.executeDownLoad();
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        log.info("全部下载完成，共耗时：{}",(System.currentTimeMillis()-startTime));
    }
}
