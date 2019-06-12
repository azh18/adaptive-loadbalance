package com.aliware.tianchi.util;

import java.util.concurrent.TimeUnit;

/**
 * 获取系统时间，防止大量调用 System.currentTimeMillis()
 */
public final class TimeUtil {

    private static volatile long currentTimeMillis;

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    currentTimeMillis = System.currentTimeMillis();
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Throwable e) {

                    }
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }

    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}