package com.www.socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * @Describtion: UdpSender
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:41
 */
public class UdpSender {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true){
           String data = reader.readLine();
            InetAddress localhost = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(data.getBytes(), 0, data.getBytes().length,localhost,8989);
            datagramSocket.send(packet);
            if ("bye".equals(data)){
                break;
            }
        }

        datagramSocket.close();
    }
}
