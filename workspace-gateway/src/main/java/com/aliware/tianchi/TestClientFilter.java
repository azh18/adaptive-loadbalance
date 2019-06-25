package com.aliware.tianchi;

import com.aliware.tianchi.util.Loops;
import com.aliware.tianchi.util.SlidingWindowCounter;
import com.aliware.tianchi.util.TimeUtil;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    public static ThreadLocal<Map<String, Map<Long, Long>>> threadLocal = new ThreadLocal<>();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            long createTime = TimeUtil.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            long endTime = TimeUtil.currentTimeMillis();
            long rt = endTime - createTime;

            if (result.hasException()) {
                System.out.println("出现异常！ invoker = " + invoker.getInterface().getName());
                rt = 1000;
            }
            SlidingWindowCounter slidingWindowCounter = Loops.windowCounterMap.get(invoker.getUrl().getHost());
            if (Objects.isNull(slidingWindowCounter)) {
                slidingWindowCounter = new SlidingWindowCounter(3);
                Loops.windowCounterMap.put(invoker.getUrl().getHost(), slidingWindowCounter);
            }
            slidingWindowCounter.increase(rt);
            return result;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
