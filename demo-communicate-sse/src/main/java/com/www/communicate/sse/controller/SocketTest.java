package com.www.communicate.sse.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketTest {
    public static void main(String[] args) throws Exception {
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
