package com.aliware.tianchi;

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
        Map<Invoker, Map<Long, Long>> invokerMapMap = TestClientFilter.threadLocal.get();
        if (Objects.isNull(invokerMapMap)) {
            return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
        }
        long key = TimeUtil.currentTimeMillis() / 1000;
        long minRT = Long.MAX_VALUE;
        Invoker minInvoker = null;
        for (Invoker invoker : invokers) {
            Map<Long, Long> longLongMap = invokerMapMap.get(invoker);
            if(Objects.isNull(longLongMap)){
                System.out.println("随机1选择的 invoker = " + minInvoker.getInterface().getName());
                return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
            }
            Long aLong = longLongMap.get(key);
            if(Objects.isNull(aLong)){
                System.out.println("随机2选择的 invoker = " + minInvoker.getInterface().getName());
                return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
            }
            if(aLong < minRT){
                minInvoker = invoker;
                minRT = aLong;
            }
        }
        System.out.println("选择的 invoker = " + minInvoker.getInterface().getName());
        return minInvoker;
    }
}
