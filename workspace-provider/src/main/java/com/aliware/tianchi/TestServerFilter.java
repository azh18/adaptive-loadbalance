package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author daofeng.xjf
 *
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
<<<<<<< HEAD
            Result result = invoker.invoke(invocation);
=======
            long startTime = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            // 计算RTT记录在CallbackServiceImpl.concurrentLinkedQueue中, 长度先写定
            CallbackServiceImpl.concurrentLinkedQueue.add(costTime);
//            if (CallbackServiceImpl.concurrentLinkedQueue.size() > 5)
//                CallbackServiceImpl.concurrentLinkedQueue.poll();
>>>>>>> master
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
