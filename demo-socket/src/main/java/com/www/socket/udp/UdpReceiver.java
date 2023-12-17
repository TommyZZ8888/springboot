package com.www.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Describtion: UdpReceive
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:46
 */
public class UdpReceiver {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(8989);
        while (true) {

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);

            datagramSocket.receive(packet);
            String str = new String(packet.getData(), 0, packet.getLength());
            if ("bye".equals(str)) {
                break;
            }
            System.out.println(str);
        }

        datagramSocket.close();

    }
}
