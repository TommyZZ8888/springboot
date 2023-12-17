package com.www.socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * @Describtion: TalkSend
 * @Author: 张卫刚
 * @Date: 2023/12/17 19:57
 */
public class TalkSend implements Runnable{

    private final int fromPort;
    private final String toIp;
    private final int toPort;
    private final DatagramSocket socket;
    private final BufferedReader reader;

    public TalkSend(int fromPort, String toIp, int toPort) {
        this.toPort = toPort;
        this.toIp = toIp;
        this.fromPort = fromPort;
        try {
            socket = new DatagramSocket(fromPort);
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("hh");
            while (true){
                try {    String data = reader.readLine();
                    byte[] datas = data.getBytes();
                    DatagramPacket packet = new DatagramPacket(datas, 0, datas.length, new InetSocketAddress(this.toIp,this.toPort));

                socket.send(packet);
                if ("bye".equals(data)){
                    break;
                }} catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        socket.close();
    }
}
