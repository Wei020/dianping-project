package com.example.blog.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4, 60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100));

    static {
        poolExecutor.allowsCoreThreadTimeOut();
    }
}
