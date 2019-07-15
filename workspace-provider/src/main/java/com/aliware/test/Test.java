//package com.aliware.test;
//
//import com.aliware.tianchi.RateLimiter;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author: Knox
// * @Date: 2019-07-15 15:19
// * @Description: You Know
// * @Version 1.0
// */
//public class Test {
//    public static void main(String[] args) {
//        RateLimiter limiter = new RateLimiter(1);
//        long timeout = 2000;
//
//        limiter.start();
//        while (true) {
//            long now;
//            if ((now = limiter.acquire()) != -1) {
//                limiter.setLimitPerSec(limiter.getLimitPerSec() * 2);
//            } else {
//                limiter.setLimitPerSec(limiter.getLimitPerSec() + 1);
//                System.out.println("limitPerSec :" + limiter.getLimitPerSec());
//                System.out.println("throw Time :" + limiter.getThrowTime());
//            }
//
//            try {
//                TimeUnit.MILLISECONDS.sleep(10);
//            } catch (Exception e) {
//
//            }
//        }
//    }
//}
