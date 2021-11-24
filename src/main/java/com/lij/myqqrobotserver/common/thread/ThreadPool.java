package com.lij.myqqrobotserver.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Celphis
 */
public class ThreadPool {
    private static final ExecutorService THREAD_POLL = new ThreadPoolExecutor(5, 10, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getPool() {
        return THREAD_POLL;
    }
}
