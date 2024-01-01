package com.www.file.download.controller;

import com.www.file.download.demo02.MultiThreadDownLoadDemo02;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @Describtion: DownLoaderController
 * @Author: 张卫刚
 * @Date: 2024/1/1 10:33
 */
@RestController
public class DownLoaderController {


    @PostMapping("download")
    public void download(String serverPath,String localPath) {
        MultiThreadDownLoadDemo02 downLoad = new MultiThreadDownLoadDemo02(URI.create(serverPath), localPath);
        downLoad.start();
    }
}
