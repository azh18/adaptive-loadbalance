package com.aliware.tianchi;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 *         <p>
 *         负载均衡扩展接口
 *         必选接口，核心接口
 *         此类可以修改实现，不可以移动类或者修改包名
 *         选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    public static ConcurrentHashMap<String, Double> providerToRTT = new ConcurrentHashMap<>();


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        if (providerToRTT.size() < 3)
            return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));

        double reciprocalSum = 0;
        for (Double d : providerToRTT.values()) {
            reciprocalSum += 1000.0 / d;
        }

        double nextD = new Random().nextDouble() * reciprocalSum;
        for (int i = 0; i < invokers.size(); i++) {
            System.out.println(invokers.get(i).getUrl().getHost());
            nextD -= 1.0 / providerToRTT.get(invokers.get(i).getUrl().getHost());
            if (nextD < 0)
                return invokers.get(i);
        }

        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));

//        Invoker invoker = invokers.get(0);
//        System.out.println("LoadBalance executed, first url: " + invoker.getUrl());
//        int active = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).getActive();
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
