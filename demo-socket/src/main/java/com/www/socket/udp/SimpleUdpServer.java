package com.www.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Describtion: SimpleUdpServer
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:26
 */
public class SimpleUdpServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(9898);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);

        datagramSocket.receive(packet);
        byte[] data = packet.getData();
        System.out.println(packet.getAddress().getHostAddress());
        System.out.println(new String(data,0,packet.getLength()));

        datagramSocket.close();
    }
}
