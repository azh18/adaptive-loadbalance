package com.aliware.tianchi;

import java.util.Timer;
import java.util.TimerTask;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public final class RateLimiter {
    private static volatile long limitPerSec;
    private static AtomicLong capacity;
    private static volatile long throwTime;
    private static final ReentrantLock lock = new ReentrantLock();
    private static AtomicLong current = new AtomicLong(0);
    private static Timer timer;

    public RateLimiter(long limitPerSec) {
        RateLimiter.limitPerSec = limitPerSec;
        RateLimiter.capacity = new AtomicLong(limitPerSec);
        throwTime = Math.round(1000.0 / limitPerSec);
    }

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                throwToken();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, throwTime);
    }

    public void setLimitPerSec(long limitPerSec) {
        lock.lock();
        try {
            RateLimiter.limitPerSec = limitPerSec;
            throwTime = Math.round(1000.0 / limitPerSec);
            timer.cancel();
            start();
        } finally {
            lock.unlock();
        }
    }

    public long acquire() {
        lock.lock();
        try {
            if (current.get() < capacity.get()) {
                return current.getAndIncrement();
            } else {
                System.out.println("overload...");
                return -1;
            }
        } finally {
            lock.unlock();
        }
    }

    private long throwToken() {
        return capacity.incrementAndGet();
    }

    public long getLimitPerSec() {
        return limitPerSec;
    }

    public long getCapacity() {
        return capacity.get();
    }

    public long getThrowTime() {
        return throwTime;
    }

    public AtomicLong getCurrent() {
        return current;
    }
}
