package com.aliware.tianchi.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class SlotBaseCounter {
    /**
     * 槽数量
     */
    private int slotSize;
    /**
     * 实际存储的数字
     */
    private volatile long[] slotCounter;

    /**
     * 生成一个有 slotSize 个槽的计数器
     *
     * @param slotSize
     */
    public SlotBaseCounter(int slotSize) {
        slotSize = slotSize < 1 ? 1 : slotSize;
        this.slotSize = slotSize;
        this.slotCounter = new long[slotSize];
        for (int i = 0; i < this.slotSize; i++) {
            slotCounter[i] = 0L;
        }
    }

    public void increaseSlot(int slotSize, long count) {
        slotCounter[slotSize] += count;
    }

    public void wipeSlot(int slotSize) {
        slotCounter[slotSize] = 0;
    }

    public long getSlot(int slotSize) {
        return slotCounter[slotSize];
    }

    @Override
    public String toString() {
        return Arrays.toString(slotCounter);
    }
}  