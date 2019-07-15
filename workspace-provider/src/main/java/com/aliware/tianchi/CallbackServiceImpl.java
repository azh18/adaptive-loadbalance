package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author daofeng.xjf
 *         <p>
 *         服务端回调服务
 *         可选接口
 *         用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    private final int period = 50; // 定期推送的周期
    private final int calculatedNum = 3; // 窗口大小
    public static ConcurrentLinkedQueue<Long> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

//    public CallbackServiceImpl() {
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (!listeners.isEmpty()) {
//                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
//                        try {
//                            entry.getValue().receiveServerMsg(System.getProperty("quota") + " " + new Date().toString());
//                        } catch (Throwable t1) {
//                            listeners.remove(entry.getKey());
//                        }
//                    }
//                }
//            }
//        }, 0, 5000);
//    }

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            // 先删去多余的rtt信息
                            for (int i = 0; i < concurrentLinkedQueue.size() - calculatedNum; i++)
                                concurrentLinkedQueue.poll();

                            double averageRTT = 0;
                            int count = 0;
                            for (Long l : concurrentLinkedQueue) {
                                averageRTT += l;
                                count++;
                            }
                            averageRTT = averageRTT / (1.0 * count);

                            // 传递quota, CallbackListenerImpl中可通过该值判断是哪个provider
//                            System.out.println(System.getProperty("quota") + " " + averageRTT);
                            entry.getValue().receiveServerMsg(System.getProperty("quota") + " " + averageRTT);
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, period);
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
