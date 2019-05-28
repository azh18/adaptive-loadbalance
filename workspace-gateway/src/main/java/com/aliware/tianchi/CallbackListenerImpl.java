package com.aliware.tianchi;

import com.aliware.tianchi.Context.Provider;
import com.aliware.tianchi.Context.ProviderStateEnum;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器 可选接口 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

  private static Context context = Context.getInstance();


  private Timer timer = null;

  private volatile int sec = 0;

  private final Object lock = new Object();

  private void init() {
    timer = new Timer();
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            synchronized (lock) {
              sec++;
            }
          }
        }
        , 1000, 1000);
  }

  @Override
  public void receiveServerMsg(String msg) {
    try {
      if(timer == null) {
        init();
      }

      int s = context.small();
      int m = context.mid();
      int l = context.large();

      String[] strings = msg.split(" ");

      System.out.println(strings[0]
          + " " + strings[1]
          + " " + strings[2]
          + " " + strings[4] + "ms"
          + " " + strings[5] + "ms"
          + " " + s
          + " " + m
          + " " + l
          + " " + sec);

      double c = Double.valueOf(strings[4]);
      double avg = Double.valueOf(strings[5]);

      String provider = strings[0];

      if ("small".equals(provider)) {
        context.adjust(state(c, avg), Provider.S);
      }
      if ("medium".equals(provider)) {
        context.adjust(state(c, avg), Provider.M);
      }
      if ("large".equals(provider)) {
        context.adjust(state(c, avg), Provider.L);
      }
    } catch (Exception e) {
      System.out.println(msg);
      // ignore
    }
  }

  private ProviderStateEnum state(double c, double avg) {
    if (c > avg) {
      if ((c - avg) / avg > 0.35) {
        return ProviderStateEnum.$6BUSY;
      }
      if ((c - avg) / avg > 0.3) {
        return ProviderStateEnum.$5BUSY;
      }
      if ((c - avg) / avg > 0.25) {
        return ProviderStateEnum.$4BUSY;
      }
      if ((c - avg) / avg > 0.2) {
        return ProviderStateEnum.$3BUSY;
      }
      if ((c - avg) / avg > 0.15) {
        return ProviderStateEnum.$2BUSY;
      }
      if ((c - avg) / avg > 0.1) {
        return ProviderStateEnum.$1BUSY;
      }
      if ((c - avg) / avg > 0.05) {
        return ProviderStateEnum.BUSY;
      }
    }

    if (c < avg) {
      if ((avg - c) / c > 0.2) {
        return ProviderStateEnum.$$$IDLE;
      }
      if ((avg - c) / c > 0.15) {
        return ProviderStateEnum.$$IDLE;
      }
      if ((avg - c) / c > 0.1) {
        return ProviderStateEnum.$IDLE;
      }
      if ((avg - c) / c > 0.05) {
        return ProviderStateEnum.IDLE;
      }
    }

    return ProviderStateEnum.NORMAL;
  }
}
