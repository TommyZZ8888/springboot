package com.www.socket.udp;

/**
 * @Describtion: Student
 * @Author: 张卫刚
 * @Date: 2023/12/17 20:14
 */
public class Student {
    public static void main(String[] args) {
        new Thread(new TalkSend(7777,"127.0.0.1",9999)).start();
        new Thread(new TalkReceive(8888,"老师")).start();
    }
}
