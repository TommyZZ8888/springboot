package com.www.zz.demozookeeper.timedtask;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IntStream.rangeClosed(1,5)
                .mapToObj(index->"机器"+index)
                .map(TimedTask::new)
                .map(task->(Runnable)()->task.go())
                .map(Thread::new)
                .forEach(Thread::start);

        TimeUnit.SECONDS.sleep(16);

        /**
         机器3等待
         机器1 创建 lock 节点成功，成为 master，定时任务由我来执行
         机器4等待
         机器5等待
         机器2等待
         机器1宕机

         机器5 创建 lock 节点成功，成 master，定时任务由我来执行
         机器4等待
         机器2等待
         机器3等待
         机器5宕机

         机器4 创建 lock 节点成功，成为 master，定时任务由我来执行
         机器2等待
         机器3等待
         机器4宕机

         机器2 创建 lock 节点成功，成为 master，定时任务由我来执行
         机器3等待
         机器2宕机

         机器3 创建 lock 节点成功，成为 master，定时任务由我来执行
         机器3宕机

         */
    }
}
