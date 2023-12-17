package com.www.socket.tcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Describtion: FileUploadClient
 * @Author: 张卫刚
 * @Date: 2023/12/17 17:52
 */
public class FileUploadClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8989);
        OutputStream outputStream = socket.getOutputStream();

        FileInputStream fileInputStream = new FileInputStream("demo-socket/src/main/resources/static/孙燕姿03.jpg");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        fileInputStream.close();
        outputStream.close();
        socket.close();
    }
}
