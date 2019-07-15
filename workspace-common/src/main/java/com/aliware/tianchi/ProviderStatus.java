package com.aliware.tianchi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zyz
 * @version 2019-07-15
 */
public class ProviderStatus {
    public static Map<String, Integer> TASK_LIMIT = new HashMap<>();
    public static Map<String, AtomicInteger> currentTask = new ConcurrentHashMap<>();
    public static Map<String, AtomicInteger> lastRTT = new ConcurrentHashMap<>();

    static {
        TASK_LIMIT.put("provider-small", 108);
        TASK_LIMIT.put("provider-medium", 331);
        TASK_LIMIT.put("provider-large", 480);
        currentTask.put("provider-small", new AtomicInteger(0));
        currentTask.put("provider-medium", new AtomicInteger(0));
        currentTask.put("provider-large", new AtomicInteger(0));
        lastRTT.put("provider-small", new AtomicInteger(0));
        lastRTT.put("provider-medium", new AtomicInteger(0));
        lastRTT.put("provider-large", new AtomicInteger(0));

    }

    public static String next() {
        boolean[] available = {true, true, true};
        currentTask.forEach((host, count) -> {
            if (count.get() >= TASK_LIMIT.get(host)) {
                available[host2number(host)] = false;
            }
        });
        AtomicReference<String> minHost = new AtomicReference<>();
        AtomicInteger minRTT = new AtomicInteger(9999999);
        lastRTT.forEach((host, rtt) -> {
            if (available[host2number(host)] && rtt.get() < minRTT.get()) {
                minRTT.set(rtt.get());
                minHost.set(host);
            }
        });
        return minHost.get();
    }

    public static void request(String host) {
        currentTask.get(host).addAndGet(1);
    }

    public static void release(String host, int rtt) {
        currentTask.get(host).addAndGet(-1);
        lastRTT.get(host).set(rtt);
    }

    private static int host2number(String host) {
        switch (host) {
            case "provider-small": return 0;
            case "provider-medium": return 1;
            case "provider-large": return 2;
        }
        return -1;
    }
}
