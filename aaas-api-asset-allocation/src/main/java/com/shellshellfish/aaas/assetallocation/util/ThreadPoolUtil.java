package com.shellshellfish.aaas.assetallocation.util;

import java.util.concurrent.*;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class ThreadPoolUtil {

    private static final int pooSize = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
        pooSize,
        pooSize,
        0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(pooSize),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy());

    public synchronized static ExecutorService getThreadPool() {
        return pool;
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
