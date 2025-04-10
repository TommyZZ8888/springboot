package com.www.file.download.controller;

import com.www.file.download.demo02.DownLoadDemo;
import com.www.file.download.demo02.MultiThreadDownLoadDemo02;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Describtion: DownLoaderController
 * @Author: 张卫刚
 * @Date: 2024/1/1 10:33
 */
@RestController
public class DownLoaderController {

    private final Map<String, DownLoadDemo> activeDownloads = new ConcurrentHashMap<>();

    @PostMapping("download")
    public void download(String serverPath,String localPath) {
        MultiThreadDownLoadDemo02 downLoad = new MultiThreadDownLoadDemo02(URI.create(serverPath), localPath);
        downLoad.start();
    }

    @PostMapping("start")
    public String download2( String serverPath,  String localPath) {
        DownLoadDemo downLoad = new DownLoadDemo(URI.create(serverPath), localPath);
        String downloadId = generateDownloadId(serverPath, localPath);
        activeDownloads.put("downloadId", downLoad);
        downLoad.start();
        return "{\"result\":\"0\",\"data\":\"Download started\",\"downloadId\":\"" + downloadId + "\"}";
    }

    @PostMapping("cancel")
    public String cancel( String downloadId) {
        DownLoadDemo downLoad = activeDownloads.get("downloadId");
//        if (downLoad != null) {
            downLoad.cancel();
            activeDownloads.remove(downloadId);
            return "{\"result\":\"0\",\"data\":\"Download cancelled\"}";
//        }
//        return "{\"result\":\"1\",\"data\":\"Download not found\"}";
    }

    @PostMapping("pause")
    public String pause(String downloadId) {
        DownLoadDemo downLoad = activeDownloads.get("downloadId");
//        if (downLoad != null) {
            downLoad.pause();
            return "{\"result\":\"0\",\"data\":\"Download paused\"}";
//        }
//        return "{\"result\":\"1\",\"data\":\"Download not found\"}";
    }

    @PostMapping("resume")
    public String resume( String downloadId) {
        DownLoadDemo downLoad = activeDownloads.get("downloadId");
//        if (downLoad != null) {
            downLoad.resume();
            return "{\"result\":\"0\",\"data\":\"Download resumed\"}";
//        }
//        return "{\"result\":\"1\",\"data\":\"Download not found\"}";
    }

    @PostMapping("restart")
    public String restart( String downloadId) {
        DownLoadDemo downLoad = activeDownloads.get("downloadId");
//        if (downLoad != null) {
            downLoad.restart();
            return "{\"result\":\"0\",\"data\":\"Download restarted\"}";
//        }
//        return "{\"result\":\"1\",\"data\":\"Download not found\"}";
    }

    private String generateDownloadId(String serverPath, String localPath) {
        return serverPath.hashCode() + "_" + localPath.hashCode();
    }
}
