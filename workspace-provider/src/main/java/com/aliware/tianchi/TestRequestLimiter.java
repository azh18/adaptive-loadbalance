package com.aliware.tianchi;

import com.aliware.tianchi.Statistics.ProviderStateEnum;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import org.apache.dubbo.common.status.Status;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.transport.RequestLimiter;
import org.apache.dubbo.rpc.protocol.dubbo.status.ThreadPoolStatusChecker;

/**
 * @author daofeng.xjf
 *
 * 服务端限流 可选接口 在提交给后端线程池之前的扩展，可以用于服务端控制拒绝请求
 */
public class TestRequestLimiter implements RequestLimiter {


  private Statistics statistics = Statistics.getInstance();

  private ThreadPoolMonitor monitor = new ThreadPoolMonitor();

  /**
   * @param request 服务请求
   * @param activeTaskCount 服务端对应线程池的活跃线程数
   * @return false 不提交给服务端业务线程池直接返回，客户端可以在 Filter 中捕获 RpcException true 不限流
   */
  @Override
  public boolean tryAcquire(Request request, int activeTaskCount) {
//        Context context = Context.getInstance();
//        if(activeTaskCount >= context.getMax()) {
//            return false;
//        }
//        context.updateMaxCur(activeTaskCount);

//        if(activeTaskCount >= i.get()) {
//            i.getAndAccumulate(n.get(), ((left, right) -> right + left));
//            n.getAndAccumulate(1, ((left, right) -> left << right));
//            return false;
//        }
//        System.out.println(activeTaskCount + " " + i.get() + " " + n.get());
//        i.getAndAccumulate(2, ((left, right) -> left - right));
//        n.set(1);

//    statistics.plus();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    monitor.check();

    boolean tooHot = statistics.tooHot();

    //    int n = random.nextInt(10);
//    ProviderStateEnum stateEnum = statistics.getStateEnum();
//    if ((stateEnum.isBusy() && n < -stateEnum.getValue()) || tooHot) {
//      System.out.println("###### reject ##### " + (-stateEnum.getValue()) + " " + tooHot);
//      return false;
//    }

    if (tooHot) {
      System.out.println("###### reject ##### " + " " + tooHot);
      return false;
    }

    return true;
  }

}
