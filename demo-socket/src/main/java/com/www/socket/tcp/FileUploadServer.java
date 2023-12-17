package com.www.socket.tcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Describtion: FileUploadServer
 * @Author: 张卫刚
 * @Date: 2023/12/17 17:52
 */
public class FileUploadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8989);
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();

        FileOutputStream fileOutputStream = new FileOutputStream(new File("demo-socket/src/main/resources/static/stefanie.jpg"));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, len);
        }

        fileOutputStream.close();
        inputStream.close();
        accept.close();
        serverSocket.close();


    }
}
