package com.www.zz.demozookeeper.order;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread payOrder = new Thread(() -> new PayOrder().payOrder(1L));
        Thread job = new Thread(() -> new TimedoutOrderJob().updateTimedoutOrder());


        /**
         * 有两种情况需要测试
         * 1、用户先获取到锁
         * 2、定时任务先获取到锁
         */


        /**
         * 用户先获取到锁
         *
         * PayOrder zookeeper 连接建立成功
         * TimedoutOrderJob zookeeper 连接建立成功
         * TimedoutOrderJob 跳过订单 orderId 1
         * TimedoutOrderJob 开始执行业务逻辑处理 orderId2
         * PayOrder 订单1 支付成功
         * 订单1支付状态：succ
         * TimedoutOrderJob 订单处理完毕 orderId2
         */
//        payOrder.start();
//        TimeUnit.SECONDS.sleep(1);
//        job.start();


        /**
         * 定时任务先获取到锁
         *
         * TimedoutOrderJob zookeeper 连接建立成功
         * TimedoutOrderJob 开始执行业务逻辑处理 orderId1
         * PayOrder zookeeper 连接建立成功
         * PayOrder 订单1 锁已存在，返回用户操作失败，请稍后重试
         * 订单1支付状态：failed
         * TimedoutOrderJob 订单处理完毕 orderId1
         * TimedoutOrderJob 开始执行业务逻辑处理 orderId2
         * TimedoutOrderJob 订单处理完毕 orderId2
         */
        job.start();
        TimeUnit.SECONDS.sleep(1);
        payOrder.start();


        TimeUnit.SECONDS.sleep(6);
    }
}
