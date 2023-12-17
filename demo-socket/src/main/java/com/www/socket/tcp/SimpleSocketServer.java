package com.www.socket.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Describtion: SocketServer1
 * @Author: 张卫刚
 * @Date: 2023/12/16 18:49
 */
public class SimpleSocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        //管道流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = inputStream.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }
        System.out.println(baos.toString());

        //方式2
//        StringBuilder sb = new StringBuilder();
//        while ((len=inputStream.read(bytes))!=-1){
//            sb.append(new String(bytes,0,len, StandardCharsets.UTF_8));
//        }
//        System.out.println(sb.toString());


        OutputStream outputStream = socket.getOutputStream();
        String message = "hi,socketClient,how you doing";
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));


        outputStream.close();
        baos.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }

    public static void test() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8089);
        Socket accept;
        while ((accept = serverSocket.accept()) != null) {
            OutputStream outputStream = accept.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String acceptString = bufferedReader.readLine();
            String[] s = acceptString.split(" ");
            String method = s[0];
            String path = s[1];
            String data = "{\"path\":\"" + path + "\",\"method\":\"" + method + "\"}";
            String result = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type:application/json\r\n" +
                    "Content-Length:" + data.length() + "\r\n" +
                    "\r\n" +
                    data + "\r\n";
            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            bufferedReader.close();
            accept.close();
        }
    }
}
