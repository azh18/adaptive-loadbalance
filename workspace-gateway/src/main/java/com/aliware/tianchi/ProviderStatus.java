package com.aliware.tianchi;

/**
 * @author jackpaul <jiekepao@gmail.com>
 * Created on 2019-07-15 19:28
 * @Description:
 */

public class ProviderStatus {
    private String hostName;
    private int availableCores;
    private long freeMemory;
    private long maxMemory;
    private boolean needStop;
    private int weight;
    private int effectiveWeight;
    private int currentWeight;

    public ProviderStatus(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getAvailableCores() {
        return availableCores;
    }

    public void setAvailableCores(int availableCores) {
        this.availableCores = availableCores;
        setWeight(this.availableCores);
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public boolean isNeedStop() {
        return needStop;
    }

    public void setNeedStop(boolean needStop) {
        this.needStop = needStop;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        setEffectiveWeight(weight);
        setCurrentWeight(0);
    }

    public int getEffectiveWeight() {
        return effectiveWeight;
    }

    public void setEffectiveWeight(int effectiveWeight) {
        this.effectiveWeight = effectiveWeight;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void increaseCurrentWeight(int number) {
        this.currentWeight += number;
    }

    public void decreaseCurrentWeight(int number) {
        this.currentWeight -= number;
    }

    public void increaseEffectiveWeight() {
        if(this.effectiveWeight < weight) {
            this.effectiveWeight ++;
        }
    }

    public void decreaseEffectiveWeight() {
        this.effectiveWeight --;
    }
    @Override
    public int hashCode() {
        return hostName.hashCode();
    }

    @Override
    public String toString() {
        return "ProviderStatus{" +
                "hostName='" + hostName + '\'' +
                ", availableCores=" + availableCores +
                ", freeMemory=" + freeMemory +
                ", maxMemory=" + maxMemory +
                ", needStop=" + needStop +
                ", weight=" + weight +
                ", effectiveWeight=" + effectiveWeight +
                ", currentWeight=" + currentWeight +
                '}';
    }
}
