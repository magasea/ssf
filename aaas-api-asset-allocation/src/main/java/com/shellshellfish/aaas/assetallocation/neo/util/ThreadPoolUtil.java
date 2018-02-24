package com.shellshellfish.aaas.assetallocation.neo.util;

import java.util.concurrent.*;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            15,
            15,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(15),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public synchronized static ExecutorService getThreadPool() {
        return pool;
    }

}
