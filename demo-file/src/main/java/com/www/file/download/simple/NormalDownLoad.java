package com.www.file.download.simple;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * @Description NormalDownLoad
 * @Author 张卫刚
 * @Date Created on 2023/12/29
 */
@Slf4j
public class NormalDownLoad {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String serverPath = "https://pics1.baidu.com/feed/8644ebf81a4c510ff3d1252bb6f37e24d42aa51a.png";
        String localPath = "demo-file/src/main/resources/static/stefanie02.png";
        new NormalDownLoad().download(serverPath, localPath);
        log.info("全部下载完成，共耗时：{}",(System.currentTimeMillis()-startTime));
    }

    public void download(String url, String localPath) {
        try {
            URL url1 = URI.create(url).toURL();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url1.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(localPath);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }

            fileOutputStream.close();
            bufferedInputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
