package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    public static ConcurrentLinkedQueue<Long> costs = new ConcurrentLinkedQueue<>();
    public static AtomicInteger activeTaskCount = new AtomicInteger(0);
//    public static ConcurrentHashMap<String, Integer> activeTasks = new ConcurrentHashMap<>();
//
//    static {
//        activeTasks.put("provider-small", 0);
//        activeTasks.put("provider-medium", 0);
//        activeTasks.put("provider-large", 0);
//    }

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    double avg = costs.stream().mapToLong(c -> c).average().orElse(0);
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
//                            String _activeTasks = activeTasks.get("provider-small") + "," + activeTasks.get("provider-medium") + "," + activeTasks.get("provider-large");
                            entry.getValue().receiveServerMsg("provider-" + System.getProperty("quota") + " " + String.format("%.4f", avg) + " " + activeTaskCount);
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 100);
    }

    public static void pushCost(long cost) {
        while (costs.size() > 4) {
            costs.remove();
        }
        costs.add(cost);
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }
}
