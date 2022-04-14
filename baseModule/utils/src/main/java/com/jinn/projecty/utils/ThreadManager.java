package com.jinn.projecty.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 此线程池最大线程数等于核心线程数 = Runtime.getRuntime().availableProcessors() * 2 + 1
 */
public class ThreadManager {
    private static ThreadManager sThreadManager;
    private ThreadPoolProxy mThreadPool;
    private final String TAG = "ThreadManager";

    private ThreadManager() {
    }

    public static ThreadManager getInstance() {
        if (sThreadManager == null) {
            synchronized (ThreadManager.class) {
                if (sThreadManager == null) {
                    sThreadManager = new ThreadManager();
                }
            }
        }
        return sThreadManager;
    }

    private void createThreadPool() {
        int corePoolSize = Math.max(1, Runtime.getRuntime().availableProcessors() * 2 + 1);
        int maximumPoolSize = corePoolSize;
        LogUtils.d(TAG,"createThreadPool,corePoolSize:"+corePoolSize+",maxPoolSize:"+maximumPoolSize);
        mThreadPool = new ThreadPoolProxy(corePoolSize, maximumPoolSize, 5000L);
    }

    public ThreadPoolProxy getThreadProxy() {
        if (mThreadPool == null) {
            createThreadPool();
        }
        return mThreadPool;
    }

    public class ThreadPoolProxy {
        private ThreadPoolExecutor executor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long aliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long aliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.aliveTime = aliveTime;
        }

        private void init() {
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, aliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(30));
        }

        public Future<Object> submit(Callable<Object> callable) {
            if (executor == null) {
                init();
            }
            return executor.submit(callable);
        }

        public void excute(Runnable runnable) {
            if (executor == null) {
                init();
            }
            executor.execute(runnable);
        }

        public void cancle(Runnable runnable) {
            if (executor != null && !executor.isShutdown() && !executor.isTerminated()) {
                executor.remove(runnable);
            }
        }
    }


}
