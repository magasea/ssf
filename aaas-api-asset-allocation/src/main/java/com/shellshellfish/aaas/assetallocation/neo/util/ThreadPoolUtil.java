package com.shellshellfish.aaas.assetallocation.neo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class ThreadPoolUtil {

    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static ExecutorService getThreadPool() {
        return pool;
    }

}
