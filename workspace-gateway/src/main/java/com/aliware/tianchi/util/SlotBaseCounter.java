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
    private AtomicInteger[] slotCounter;

    /**
     * 生成一个有 slotSize 个槽的计数器
     *
     * @param slotSize
     */
    public SlotBaseCounter(int slotSize) {  
        slotSize = slotSize < 1 ? 1 : slotSize;  
        this.slotSize = slotSize;  
        this.slotCounter = new AtomicInteger[slotSize];  
        for (int i = 0; i < this.slotSize; i++) {  
            slotCounter[i] = new AtomicInteger(0);  
        }  
    }  
  
    public void increaseSlot(int slotSize) {  
        slotCounter[slotSize].incrementAndGet();  
    }  
  
    public void wipeSlot(int slotSize) {  
        slotCounter[slotSize].set(0);  
    }  
  
    public int totalCount() {  
        return Arrays.stream(slotCounter).mapToInt(slotCounter -> slotCounter.get()).sum();
    }  
  
    @Override  
    public String toString() {  
        return Arrays.toString(slotCounter);  
    }  
}  