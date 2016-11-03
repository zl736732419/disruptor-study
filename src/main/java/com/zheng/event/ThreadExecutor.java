package com.zheng.event;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程执行器
 * Created by zhenglian on 2016/11/3.
 */
public class ThreadExecutor {
    private static AtomicInteger poolNo = new AtomicInteger(0);

    public static ThreadFactory createThreadFactory() {
        ThreadFactory factory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                SecurityManager manager = System.getSecurityManager();
                ThreadGroup group = manager != null ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
                Thread thread = new Thread(group, r, "Thread-" + poolNo.getAndIncrement() + "-");
                if (thread.isDaemon()) {
                    thread.setDaemon(Boolean.FALSE);
                }
                if (thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        };

        return factory;
    }
}
