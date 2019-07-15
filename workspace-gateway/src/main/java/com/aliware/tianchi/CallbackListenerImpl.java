package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 *
 */
public class CallbackListenerImpl implements CallbackListener {

    @Override
    public void receiveServerMsg(String msg) {
//        System.out.println("receive msg from server :" + msg);
        if (msg.split(" ")[1] == null || !Character.isDigit(msg.split(" ")[1].charAt(0)))
            return;
        String[] parts = msg.split(" ");
        String host = parts[0];
        int rtt = (int) Double.parseDouble(parts[1]);
        int activeTaskCount = Integer.parseInt(parts[2]);

        ProviderStatus.refresh(host, rtt);
        ProviderStatus.active(host, activeTaskCount);
    }

}
