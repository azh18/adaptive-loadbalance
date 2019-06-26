package com.aliware.tianchi;

import com.aliware.tianchi.util.Loops;
import com.aliware.tianchi.util.SlidingWindowCounter;
import com.aliware.tianchi.util.TimeUtil;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.sql.Time;
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


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            long start = TimeUtil.currentTimeMillis();
            invocation.getAttachments().put("start", start + "");
            return invoker.invoke(invocation);
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        long start = Long.valueOf(invocation.getAttachment("start"));
        long endTime = TimeUtil.currentTimeMillis();
        long rt = endTime - start;
        SlidingWindowCounter slidingWindowCounter = Loops.windowCounterMap.get(invoker.getUrl().getHost());

        if (result.hasException()) {
//            StringBuilder stringBuilder = new StringBuilder();
//            Loops.windowCounterMap.forEach((s, tmpSlidingWindowCounter) -> {
//                stringBuilder.append(s).append(":").append(tmpSlidingWindowCounter.get()).append(" ").append(tmpSlidingWindowCounter.getExceptionCount()).append(" ");
//            });
//            System.out.println(TimeUtil.currentTimeMillis() + " 出现异常！ invoker = " + invoker.getUrl().getHost() + " info:" + stringBuilder);
            rt = 1000;
            slidingWindowCounter.addException();
        }
        if (Objects.isNull(slidingWindowCounter)) {
            slidingWindowCounter = new SlidingWindowCounter(3);
            Loops.windowCounterMap.put(invoker.getUrl().getHost(), slidingWindowCounter);
        }
        slidingWindowCounter.increase(rt * slidingWindowCounter.getExceptionCount());
        return result;
    }
}
