package com.aliware.tianchi.util;

/**
 * 滑动窗口类
 */
public class SlidingWindowCounter {
    /**
     * 计数类
     */
    private volatile SlotBaseCounter slotBaseCounter;
    /**
     * 窗口大小
     */
    private volatile int windowSize;
    /**
     * 当前位置
     */
    private volatile int head;

    /**
     * 构造方法，生成一个窗口大小为 windowSize 的滑动窗口
     *
     * @param windowSize 窗口大小
     */
    public SlidingWindowCounter(int windowSize) {
        resizeWindow(windowSize);
    }

    /**
     * 重置窗口大小为windowSize
     *
     * @param windowSize 窗口大小
     */
    public synchronized void resizeWindow(int windowSize) {
        this.windowSize = windowSize;
        this.slotBaseCounter = new SlotBaseCounter(windowSize);
        this.head = 0;
    }

    public void increase(long count) {
        slotBaseCounter.increaseSlot(head, count);
    }


    public void advance() {
        int tail = (head + 1) % windowSize;
        slotBaseCounter.wipeSlot(tail);
        head = tail;
    }

    public long get(){
        return slotBaseCounter.getSlot(head);
    }

    @Override
    public String toString() {
        return  " head = " + head + " >> " + slotBaseCounter;
    }
}  