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
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (Throwable ignored) {
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();

    }

  
    public static void fixLoop(Runnable runnable, int loop) {
        for (int i = 0; i < loop; i++) {
            run(runnable);  
        }  
    }  
  
    private static void run(Runnable runnable) {  
        try {  
            runnable.run();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  