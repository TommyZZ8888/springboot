package com.www.socket.udp;

import java.io.IOException;
import java.net.*;

/**
 * @Describtion: SimpleUdpClient
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:22
 */
public class SimpleUdpClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        String msg ="hi,i am udp client";

        InetAddress localhost = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), 0, msg.getBytes().length, localhost, 9898);

        datagramSocket.send(packet);

        datagramSocket.close();
    }
}
