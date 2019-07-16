package com.aliware.tianchi;

import org.omg.CORBA.INTERNAL;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jackpaul <jiekepao@gmail.com>
 * Created on 2019-07-15 15:05
 * @Description:
 */



public class GlobalParameters {

    //key 都是  quota
//    static ConcurrentHashMap<String, Boolean> needStop;
//    static ConcurrentHashMap<String, Integer> weights;
//
//    static {
//        needStop = new ConcurrentHashMap<>();
//        weights = new ConcurrentHashMap<>();
//    }
    static ConcurrentHashMap<String, ProviderStatus> status;
//    static ConcurrentHashMap<String, Integer> weights;
    static Map<String, String> quotaMap;
    static {
        status = new ConcurrentHashMap<>();
//        quotaMap = new HashMap<>();
//        weights = new ConcurrentHashMap<>();
    }

}
