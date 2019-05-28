package com.aliware.tianchi;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.status.Status;
import org.apache.dubbo.common.status.StatusChecker;
import org.apache.dubbo.common.store.DataStore;

/**
 * @author zrj CreateDate: 2019/5/28
 */

public class ThreadPoolMonitor {

  private Timer timer = new Timer();

  private static Statistics statistics = Statistics.getInstance();

  DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
  Map<String, Object> executors = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);

  public ThreadPoolMonitor() {
    check();
  }

  public void check() {

    for (Map.Entry<String, Object> entry : executors.entrySet()) {
      ExecutorService executor = (ExecutorService) entry.getValue();

      if (executor instanceof ThreadPoolExecutor) {
        ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
        statistics.setMax(tp.getMaximumPoolSize(), tp.getActiveCount());
      }
    }
  }
}
