package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
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
    private static Random random = new Random(2019);

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
//        String host = ProviderStatus.next();
//        for (Invoker<T> invoker : invokers) {
//            if (invoker.getUrl().getHost().equals(host)) {
//                return invoker;
//            }
//        }
//        System.out.println("out of pool");
//        return null;
        int r = random.nextInt(9);
        String host;
        if (r < 1) {
            host = "provider-small";
        } else if (r < 4) {
            host = "provider-medium";
        } else {
            host = "provider-large";
        }
        for (Invoker<T> invoker : invokers) {
            if (invoker.getUrl().getHost().equals(host)) {
                return invoker;
            }
        }
        return null;
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
