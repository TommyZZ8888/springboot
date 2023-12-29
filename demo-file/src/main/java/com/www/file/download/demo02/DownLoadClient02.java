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
        String serverPath = "https://pics1.baidu.com/feed/8644ebf81a4c510ff3d1252bb6f37e24d42aa51a.png";
        String localPath = "demo-file/src/main/resources/static/stefanie.png";
        MultiThreadDownLoadDemo02 downLoad = new MultiThreadDownLoadDemo02(URI.create(serverPath), localPath);
        long startTime = System.currentTimeMillis();
        downLoad.start();
        log.info("全部下载完成，共耗时：{}",(System.currentTimeMillis()-startTime));
    }
}
