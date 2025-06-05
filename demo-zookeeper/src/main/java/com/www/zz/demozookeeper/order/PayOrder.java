package com.www.zz.demozookeeper.order;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PayOrder {
    private static final String timeOutOrderLockPrefix = "/timeOutOrderLock";
    public void payOrder(Long orderId) {
        /**
         * 1、连上 zookeeper
         * 2、开始支付订单
         */
        CountDownLatch latch = new CountDownLatch(1);
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    latch.countDown();
                }
            });
            latch.await();
            System.out.println("PayOrder zookeeper 连接建立成功");

            /**
             * 尝试去支付订单
             */
            String payStatus = startPay(zooKeeper, orderId);
            System.out.println("订单"+orderId+"支付状态："+payStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String  startPay(ZooKeeper zooKeeper, Long orderId) {
        /**
         * 支付之前，需要加锁。尝试创建 /timeOutOrderLock/{orderId} 节点
         *      如果创建成功 ，则进行付款操作
         *      如果创建失败，则快速失败，告诉用户稍后重试
         */
        String lockPath = timeOutOrderLockPrefix + "/" + orderId;

        try {
            zooKeeper.create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            // 执行付款操作
            TimeUnit.SECONDS.sleep(3);
            zooKeeper.delete(lockPath,-1);
            System.out.println("PayOrder 订单"+orderId+" 支付成功");
            return "succ";
        }catch (KeeperException.NodeExistsException e){
            System.out.println("PayOrder 订单"+orderId+" 锁已存在，返回用户操作失败，请稍后重试");
            return "failed";
        }catch (Exception e) {
            /**
             * 其实还有其他的状态，比如连接断开，会话过期等等
             * 本次课程主要是让大家明白 master 选举的原理。
             * 我们只关心主要流程，其他的异常情况就不做处理了
             */
            System.out.println("PayOrder startPay 异常状态"+e.getMessage());
            return "error";
        }

    }
}
