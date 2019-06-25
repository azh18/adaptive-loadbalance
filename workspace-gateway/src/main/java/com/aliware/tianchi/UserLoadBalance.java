package com.aliware.tianchi;

import com.aliware.tianchi.util.Loops;
import com.aliware.tianchi.util.SlidingWindowCounter;
import com.aliware.tianchi.util.TimeUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        Invoker minInvoker = invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
        long min = Long.MAX_VALUE;
        for (Invoker invoker : invokers) {
            SlidingWindowCounter slidingWindowCounter = Loops.windowCounterMap.get(invoker.getUrl().getHost());
            if (Objects.isNull(slidingWindowCounter)) {
                System.out.println("没有找到slidingWindowCounter，选择随机的：" + invoker.getUrl().getHost());
                return minInvoker;
            }
            long tmp = slidingWindowCounter.get();
            if(tmp < min){
                min = tmp;
                minInvoker = invoker;
            }
        }
//        System.out.println("选择10ms内最小的：" + minInvoker.getUrl().getHost()  + " " + min);
        return minInvoker;

    }
}
