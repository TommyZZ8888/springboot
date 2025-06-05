package com.www.zz.demozookeeper.timedtask;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TimedTask {
    private static final String lockPath = "/timedTask/lock";
    private String machineName;

    public TimedTask(String machineName) {
        this.machineName = machineName;
    }

    public void go() {
        /**
         * 连接上 Zookeeper 集群
         */
        CountDownLatch latch = new CountDownLatch(1);
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2184,127.0.0.1:2182,127.0.0.1:2183", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    latch.countDown();
                }
            });
            latch.await();
            System.out.println(machineName+" zookeeper 连接建立成功" + zooKeeper);

            /**
             * 尝试成为 master
             */
            toBeAMaster(zooKeeper,machineName);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void toBeAMaster(ZooKeeper zooKeeper, String machineName) {
        /**
         * 1、尝试去创建 /timedTask/lock 临时无序节点
         *      如果创建成功，则可以执行定时任务
         *      如果创建失败，则监听 （wacher） /timedTask/lock 节点
         * 2、如果收到节点被删除的通知，就重新执行步骤 1
         */
        zooKeeper.create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                if (rc == KeeperException.Code.OK.intValue()) {
                    System.out.println(machineName+" 创建 lock 节点成功，成为 master，定时任务由我来执行");
                    try {
                        // 执行定时任务
                        TimeUnit.SECONDS.sleep(3);
                        zooKeeper.delete(lockPath, -1);
                        System.out.println(machineName+"宕机");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (rc == KeeperException.Code.NODEEXISTS.intValue()) {
                    System.out.println(machineName+"等待");
                    try {
                        zooKeeper.exists(lockPath, new Watcher() {
                            @Override
                            public void process(WatchedEvent event) {
                                if (event.getType() == Event.EventType.NodeDeleted) {
                                    toBeAMaster(zooKeeper, machineName);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    /**
                     * 其实还有其他的状态，比如连接断开，会话过期等等
                     * 本次课程主要是让大家明白 master 选举的原理。
                     * 我们只关心主要流程，其他的异常情况就不做处理了
                     */
                    System.out.println(machineName+"toBeAMaster 异常状态" + rc);
                }
            }
        }, "ctx_data");
    }
}
