package com.aliware.tianchi;

public final class TrafficControlCwnd {
    private int cwnd;
    private int ssthresh;
    private int timeoutCnt;
    private int timeoutThreshold;

    public TrafficControlCwnd(int initCwnd, int timeoutThreshold, int ssthresh){
        this.cwnd = initCwnd;
        this.ssthresh = ssthresh;
        this.timeoutThreshold = timeoutThreshold;
    }

    public int getCurrentCwnd(boolean lastSuccess) throws Exception{
        int ret = this.refreshCwnd(lastSuccess);
        if(ret != 0){
            throw new Exception(String.format("refresh cwnd return %d\n", ret));
        }
        return this.cwnd;
    }

    private int refreshCwnd(boolean lastSuccess){
        int nextCwnd = cwnd;
        if(lastSuccess){
            if((nextCwnd << 1) > this.ssthresh){
                nextCwnd += 1;
            } else {
                nextCwnd = nextCwnd << 1;
            }
            this.timeoutCnt = 0;
        } else {
            this.timeoutCnt += 1;
            if(this.timeoutCnt > this.timeoutThreshold){
                this.ssthresh = nextCwnd >> 1;
                nextCwnd = this.ssthresh;
            }
        }
        this.cwnd = nextCwnd;
        System.out.printf("cwnd=%d,ssthresh=%d,lastSuccess=%b\t", this.cwnd, this.ssthresh, lastSuccess);
        return 0;
    }
}
