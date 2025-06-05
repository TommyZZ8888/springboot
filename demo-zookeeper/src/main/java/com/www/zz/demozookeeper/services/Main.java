package com.www.zz.demozookeeper.services;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Arrays.asList("192.168.100.10","192.168.100.11")
                .stream()
                .map(ip->(Runnable)()->new UserService().userService(ip))
                .map(Thread::new)
                .forEach(Thread::start);

        TimeUnit.MINUTES.sleep(20);



        /**
         * 1、服务启动的时候，可以注册到 服务注册中心
         UserService 192.168.100.11 连接建立成功
         UserService 192.168.100.10 连接建立成功
         UserService 192.168.100.11 上线成功。Zookeeper 节点路径是/services/user/192.168.100.11
         UserService 192.168.100.10 上线成功。Zookeeper 节点路径是/services/user/192.168.100.10

         UserService 第一次192.168.100.10发现可用的 order 服务192.168.101.10
         UserService 第一次192.168.100.11发现可用的 order 服务192.168.101.10


         2、注册到服务中心的服务，可以被其他服务发现
         UserService 192.168.100.10发现可用的 order 服务192.168.101.11
         UserService 192.168.100.10发现可用的 order 服务192.168.101.10
         UserService 192.168.100.11发现可用的 order 服务192.168.101.11
         UserService 192.168.100.11发现可用的 order 服务192.168.101.10

         3、当服务下线的时候，对这个服务感兴趣的服务 会收到通知
         UserService 192.168.100.11发现可用的 order 服务192.168.101.11
         UserService 192.168.100.10发现可用的 order 服务192.168.101.11
         */
    }
}
