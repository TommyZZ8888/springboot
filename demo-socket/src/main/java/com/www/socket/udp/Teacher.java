package com.www.socket.udp;

/**
 * @Describtion: Teacher
 * @Author: 张卫刚
 * @Date: 2023/12/17 20:15
 */
public class Teacher {
    public static void main(String[] args) {
        new Thread(new TalkSend(5555, "127.0.0.1", 8888)).start();
        new Thread(new TalkReceive(9999, "student")).start();
    }
}
