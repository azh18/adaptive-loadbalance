package com.aliware.tianchi;

import com.aliware.tianchi.util.TimeUtil;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 *
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    public static ThreadLocal<Map<Invoker,Map<Long,Long>>> threadLocal = new ThreadLocal<>();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
            Long createTime = TimeUtil.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            long endTime = TimeUtil.currentTimeMillis();
            long rt = endTime - createTime;
            if(result.hasException()){
                rt = 2000;
            }
            Map<Invoker, Map<Long, Long>> invokerMapMap = threadLocal.get();
            if(invokerMapMap == null){
                invokerMapMap = new ConcurrentHashMap<>();
                threadLocal.set(invokerMapMap);
            }
            Map<Long, Long> longLongMap = invokerMapMap.get(invoker);
            if(Objects.isNull(longLongMap)){
                longLongMap = new ConcurrentHashMap<>();
                invokerMapMap.put(invoker,longLongMap);
            }
            long key = endTime / 1000;
            Long aLong = longLongMap.get(key);
            if(Objects.isNull(aLong)){
                longLongMap.put(key,rt);
            }else{
                longLongMap.put(key,aLong + rt);
            }
            return result;
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
