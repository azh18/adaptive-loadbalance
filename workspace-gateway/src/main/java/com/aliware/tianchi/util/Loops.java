package com.aliware.tianchi.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Loops {

    public static Map<String,SlidingWindowCounter> windowCounterMap = new ConcurrentHashMap<>();

    static {
        Thread daemon = new Thread(() -> {
            while (true) {
                try {
                    windowCounterMap.forEach((s, slidingWindowCounter) -> {
                        slidingWindowCounter.advance();
                    });
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (Throwable ignored) {
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();

    }
  
}  