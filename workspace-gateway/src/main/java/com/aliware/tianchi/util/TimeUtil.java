package com.aliware.tianchi.util;

import com.aliware.tianchi.TestClientFilter;
import org.apache.dubbo.rpc.Invoker;

import java.util.Map;
import java.util.Objects;
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
                    } catch (Throwable ignored) {

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