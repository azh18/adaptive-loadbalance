package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

    public static long readFileInt(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // int line = 1;
            // 一次读入一行，直到读入null为文件结束
            // while ((tempString = reader.readLine()) != null) {
            //     // 显示行号
            //     System.out.println("line " + line + ": " + tempString);
            //     line++;
            // }
            if ((tempString = reader.readLine()) == null){
                return 0;
            }
            reader.close();
            // System.out.println(tempString.length());
            return Long.parseLong(tempString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return 0;
    }

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            long used_mem = readFileInt("/sys/fs/cgroup/memory/memory.usage_in_bytes");
                            long limit_mem = readFileInt("/sys/fs/cgroup/memory/memory.limit_in_bytes");
                            long free_mem = limit_mem - used_mem;
                            entry.getValue().receiveServerMsg(System.getProperty("quota") + ":" + Runtime.getRuntime().availableProcessors() + ":" + Long.toString(limit_mem) + ":" + Long.toString(free_mem));
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 5000);
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
