package com.www.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Describtion: TalkReceive
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:58
 */
public class TalkReceive implements Runnable {

    private final int port;
    private final String msgFrom;

    private DatagramSocket socket;

    public TalkReceive(int port, String msgFrom) {
        this.port = port;
        this.msgFrom = msgFrom;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                socket.receive(packet);

                byte[] data = packet.getData();
                String str = new String(data, 0, packet.getLength());
                System.out.println(msgFrom + ": " + str);
                if ("bye".equals(str)) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        socket.close();
    }
}
