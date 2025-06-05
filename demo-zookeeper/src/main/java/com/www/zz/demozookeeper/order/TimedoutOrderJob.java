package com.www.zz.demozookeeper.order;

import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TimedoutOrderJob {
    private static final String timeOutOrderLockPrefix = "/timeOutOrderLock";

    public void updateTimedoutOrder() {
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
            System.out.println("TimedoutOrderJob zookeeper 连接建立成功");

            /**
             * 尝试成为 master
             */
            startUpdateTimedOrder(zooKeeper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startUpdateTimedOrder(ZooKeeper zooKeeper) {
        /**
         * 1、找出 30 分钟内没有付款的订单
         * 2、处理每一条订单的时候，加上分布式锁。尝试创建 /timeOutOrderLock/{orderId} 节点
         *      如果创建成功，则进行业务逻辑处理
         *      如果创建失败，则跳过该笔订单，不作处理
         * 3、修改 需要修改状态的 订单
         */
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, "NOT_PAY"));
        orders.add(new Order(2L, "NOT_PAY"));

        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            String lockPath = timeOutOrderLockPrefix + "/" + order.getId();
            zooKeeper.create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    if (rc == KeeperException.Code.OK.intValue()) {
                        System.out.println("TimedoutOrderJob 开始执行业务逻辑处理 orderId" + order.getId());
                        try {
                            TimeUnit.SECONDS.sleep(3);
                            zooKeeper.delete(lockPath, -1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("TimedoutOrderJob 订单处理完毕 orderId" + order.getId());
                    } else if (rc == KeeperException.Code.NODEEXISTS.intValue()) {
                        System.out.println("TimedoutOrderJob 跳过订单 orderId" + order.getId());
                        iterator.remove();
                    } else {
                        /**
                         * 其实还有其他的状态，比如连接断开，会话过期等等
                         * 本次课程主要是让大家明白 master 选举的原理。
                         * 我们只关心主要流程，其他的异常情况就不做处理了
                         */
                        System.out.println("TimedoutOrderJob 异常状态" + rc);
                    }
                }
            }, "callback_data");

        }


    }

    private class Order {
        private Long id;
        private String status;

        public Order(Long id, String status) {
            this.id = id;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
