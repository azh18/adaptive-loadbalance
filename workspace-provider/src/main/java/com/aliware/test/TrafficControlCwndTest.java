package com.aliware.test;

import java.util.Random;
import com.aliware.tianchi.TrafficControlCwnd;

public class TrafficControlCwndTest {
    public static void main(String[] args){
        TrafficControlCwnd cwndGetter = new TrafficControlCwnd(10, 2, 30);
        for(int i=0;i<100;i++){
            Random r = new Random();
            double u = r.nextDouble();
            boolean lastSuccess = true;
            if(u > 0.6){
                lastSuccess = false;
            }
            try {
                int currentCwnd = cwndGetter.getCurrentCwnd(lastSuccess);
                System.out.printf("current cwnd: %d\n", currentCwnd);
            } catch (Exception e){
                System.out.printf("exception");
            }
        }
    }
}
