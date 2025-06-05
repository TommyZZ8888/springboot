package com.www.zz.demozookeeper.services;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UserService {
    private static final String userServicePrefix = "/services/user";
    private static final String orderServicePrefix = "/services/order";

    public void userService(String ip) {
        /**
         * 1、连上 Zookeeper
         * 2、注册到服务注册中心
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
            System.out.println("UserService " + ip + " 连接建立成功");

            /**
             * 把自己 注册到服务注册中心
             */
            startRegister(zooKeeper, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startRegister(ZooKeeper zooKeeper, String ip) {
        /**
         * 1、尝试创建 /services/user/{ip} 节点
         *      如果创建成功，表示正常注册到服务注册中心
         *      如果创建失败，直接退出，去查 ip 被占用的原因
         * 2、如果正常注册到服务注册中心。由于用户服务 需要调用 订单服务，
         *      所以，需要监听订单服务。一旦订单服务的列表发生变化（新增、宕机），这里就会及时感知到
         *
         *
         */
        zooKeeper.create(userServicePrefix + "/" + ip, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                if (rc == KeeperException.Code.OK.intValue()) {
                    System.out.println("UserService " + ip + " 上线成功。Zookeeper 节点路径是" + name);
                    try {
                        List<String> orderServiceIPs = autoFindOrderService(zooKeeper, ip);

                        if (orderServiceIPs.size() < 1) {
                            System.out.println("UserService 第一次" + ip + " 没有发现可用的 order 服务");
                        } else {
                            orderServiceIPs.stream()
                                    .map(orderIp -> "UserService 第一次" + ip + "发现可用的 order 服务" + orderIp)
                                    .forEach(System.out::println);
                        }

                    } catch (Exception e) {
                        System.out.println("UserService 第一次" + ip + " 获取 order 服务异常");
                    }
                } else {
                    System.out.println("startRegister 状态异常"+rc);
                }
            }

            private List<String> autoFindOrderService(ZooKeeper zooKeeper, String ip) throws KeeperException, InterruptedException {

                    return zooKeeper.getChildren(orderServicePrefix, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                                /**
                                 * 这个事件表示 子节点发生了变化
                                 * 一旦发生变化，我们就需要获取最新的 子节点列表  并且  继续监听 订单节点
                                 */
                                try {
                                    List<String> orderServiceIPs = autoFindOrderService(zooKeeper, ip);
                                    if (orderServiceIPs.size() < 1) {
                                        System.out.println("UserService " + ip + " 没有发现可用的 order 服务");
                                    } else {
                                        orderServiceIPs.stream()
                                                .map(orderIp -> "UserService " + ip + "发现可用的 order 服务" + orderIp)
                                                .forEach(System.out::println);
                                    }
                                } catch (Exception e) {
                                    System.out.println("UserService " + ip + " 获取 order 服务异常");
                                }
                            } else {
                                /**
                                 * 我们目前只关注 子节点变化的事件，其他事件先不关注
                                 */
                                System.out.println("其他事件："+event.getType().getIntValue()+event.getState().getIntValue());
                            }
                        }
                    });
            }
        }, "callback data");
    }
}
