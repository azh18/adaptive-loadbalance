package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    public static AtomicInteger requestCount = new AtomicInteger(0);

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
//        requestCount.addAndGet(1);
        String host = ProviderStatus.next();
        for (Invoker<T> invoker : invokers) {
            if (invoker.getUrl().getHost().equals(host)) {
//                if (requestCount.get() % 100 == 0) {
//                    System.out.println(ProviderStatus.pastStat());
//                }
                return invoker;
            }
        }
        System.out.println("out of pool");
        return null;
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
