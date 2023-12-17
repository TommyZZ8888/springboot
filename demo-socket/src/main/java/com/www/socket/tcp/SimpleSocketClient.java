package com.www.socket.tcp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Describtion: SocketClient1
 * @Author: 张卫刚
 * @Date: 2023/12/16 18:42
 */
public class SimpleSocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);

        OutputStream outputStream = socket.getOutputStream();
        String msg = "hi,i am socketClient";
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        }
        System.out.println(sb.toString());

        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
