package com.shellshellfish.aaas.assetallocation.util;

import java.util.concurrent.*;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class ThreadPoolUtil {

    private static final int QUEUE_SIZE = 20;
    private static final int KEEP_ALIVE_TIME = 5;
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static final int MAX_POOL_SIZE = 2 * CORE_POOL_SIZE;

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(QUEUE_SIZE),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public synchronized static ExecutorService getThreadPool() {
        return pool;
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
