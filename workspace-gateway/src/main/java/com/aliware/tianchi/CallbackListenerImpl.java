package com.aliware.tianchi;

import com.aliware.tianchi.Context.Provider;
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

      int state = Integer.valueOf(strings[1]);
      ProviderStateEnum stateEnum = ProviderStateEnum.getFromValue(state);

      System.out.println(strings[0]
          + " " + stateEnum.toString()
          + " " + s
          + " " + m
          + " " + l
          + " " + sec);

      String provider = strings[0];

      if ("small".equals(provider)) {
        context.adjust(stateEnum, Provider.S);
      }
      if ("medium".equals(provider)) {
        context.adjust(stateEnum, Provider.M);
      }
      if ("large".equals(provider)) {
        context.adjust(stateEnum, Provider.L);
      }
    } catch (Exception e) {
      System.out.println(msg);
      // ignore
    }
  }

}
