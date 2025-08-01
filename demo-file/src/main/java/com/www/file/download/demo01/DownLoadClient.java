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
        int threadSize = 200;
//        String serverPath = "https://www.v2rayn-cn.com/v2rayN-With-Core.zip";
//        String localPath = "D:\\Program Files\\v2rayN\\v2rayN-With-Core.zip";

        String serverPath = "https://mirror.lzu.edu.cn/debian-cd/12.11.0/amd64/iso-dvd/debian-12.11.0-amd64-DVD-1.iso";
        String localPath = "E:\\vmware\\debian-12.11.0-amd64-DVD-1.iso";
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
