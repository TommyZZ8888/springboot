package com.www.file.download.demo02;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * @Description DownLoadService
 * @Author 张卫刚
 * @Date Created on 2023/12/28
 */
@Slf4j
public class DownLoadClient02 {
    public static void main(String[] args) {
        String serverPath = "https://github.com/cmderdev/cmder/releases/download/v1.3.24/cmder.zip";
        String localPath = "D:\\Program Files\\cmder\\cmder.zip";
        MultiThreadDownLoadDemo02 downLoad = new MultiThreadDownLoadDemo02(URI.create(serverPath), localPath);
        long startTime = System.currentTimeMillis();
        downLoad.start();
        log.info("全部下载完成，共耗时：{}",(System.currentTimeMillis()-startTime));

        String localPath1 = "D:\\Program Files\\cmder\\cmder.zip";

    }
}
