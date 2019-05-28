package com.aliware.tianchi;

import org.apache.dubbo.common.status.Status;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.protocol.dubbo.status.ThreadPoolStatusChecker;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 *
 * 服务端回调服务 可选接口 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

  private Statistics statistics = Statistics.getInstance();

  public CallbackServiceImpl() {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (!listeners.isEmpty()) {
          long finished = statistics.getFinished();
          long cost = statistics.getCost();
          long totalFinished = statistics.getTotalFinished();
          long totalCost = statistics.getTotalCost();

          for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
            try {
              statistics.setState(cost * 1.0 / finished, totalCost * 1.0 / totalFinished);

              entry.getValue().receiveServerMsg(
                  statistics.getName()
                      + " "
                      + String.valueOf(statistics.getRequest())
                      + " "
                      + String.valueOf(finished)
                      + " "
                      + String.valueOf(cost)
                      + " "
                      + String.format("%.2f", cost * 1.0 / finished)
                      + " "
                      + String.format("%.2f", totalCost * 1.0 / totalFinished)
              );
            } catch (Throwable t1) {
              listeners.remove(entry.getKey());
            }
          }
        }
        statistics.clear();
      }
    }, 200, 200);
  }

  //
  private Timer timer = new Timer();

  /**
   * key: listener type value: callback listener
   */
  private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

  @Override
  public void addListener(String key, CallbackListener listener) {
    listeners.put(key, listener);
    listener.receiveServerMsg(new Date().toString()); // send notification for change
  }

}
