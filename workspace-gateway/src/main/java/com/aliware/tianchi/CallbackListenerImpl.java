package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;

import static com.aliware.tianchi.GlobalParameters.status;

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
        //System.out.println("receive msg from server :" + msg);
//        String[] parameters = msg.split("\\:");
//        String providerName = parameters[0];
//        int availableCores = Integer.parseInt(parameters[1]);
//        long maximumMemory = Long.parseLong(parameters[2]);
//        long freeMemory = Long.parseLong(parameters[3]);
//        if(!status.containsKey(providerName)) {
//            status.put(providerName, new ProviderStatus(providerName));
//        }
//        ProviderStatus providerStatus = status.get(providerName);
//        if(availableCores != providerStatus.getAvailableCores()) {
//            providerStatus.setAvailableCores(availableCores);
//        }
//        providerStatus.setFreeMemory(freeMemory);
//        providerStatus.setMaxMemory(maximumMemory);
        //System.out.println(status);
    }
}
