package com.www.task.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description MyThreadFactory
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
public class MyThreadFactory implements ThreadFactory {

    public static ThreadFactory create(String namePrefix){
        return new MyThreadFactory(namePrefix);
    }

    private final String namePreFix;

    private final ThreadGroup threadGroup;

    private final AtomicInteger poolNumber = new AtomicInteger(0);

    private final AtomicInteger threadNumber = new AtomicInteger(0);

    public MyThreadFactory(String namePreFix) {
        threadGroup = Thread.currentThread().getThreadGroup();
        this.namePreFix = namePreFix + " pool " + poolNumber.incrementAndGet() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, namePreFix + threadNumber.incrementAndGet(), 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
