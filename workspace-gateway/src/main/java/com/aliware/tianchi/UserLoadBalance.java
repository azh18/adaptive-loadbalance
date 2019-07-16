package com.aliware.tianchi;

import static com.aliware.tianchi.GlobalParameters.status;

import org.apache.dubbo.common.Node;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {

    //public static List<Node>
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {

        long totalWeight = status.values().stream().collect(Collectors.summarizingInt(status -> status.getEffectiveWeight())).getSum();
        String providerName = "";
        int maxWeight = 0;

        for (Map.Entry<String, ProviderStatus> entry:status.entrySet()) {
            ProviderStatus providerStatus = entry.getValue();
            providerStatus.increaseCurrentWeight(providerStatus.getEffectiveWeight());
            if(providerStatus.getCurrentWeight() > maxWeight) {
                maxWeight = providerStatus.getCurrentWeight();
                providerName = entry.getKey();
            }
        };

        System.out.println(providerName);
        for(Invoker<T> invoker: invokers) {
            if(invoker.getUrl().getHost().indexOf(providerName) != -1) {
                status.get(providerName).decreaseCurrentWeight((int) totalWeight);
                return invoker;
            }
        }

        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
