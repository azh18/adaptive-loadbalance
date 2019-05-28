package com.aliware.tianchi;


/**
 * @author zrj CreateDate: 2019/5/26
 */
public class Statistics {

  private static Statistics instance = new Statistics();

  private static String name = System.getProperty("quota");

  public String getName() {
    return name;
  }

  public static Statistics getInstance() {
    return instance;
  }

  private final Object lock0 = new Object();

  private final Object lock1 = new Object();

  private final Object lock2 = new Object();

  private volatile long request = 0;

  private volatile long finished = 0;

  private volatile long totalFinished = 0;

  private volatile long cost = 0;

  private volatile long totalCost = 0;

  public void clear() {
    synchronized (lock0) {
      request = 0;
    }
    synchronized (lock1) {
      finished = 0;
    }
    synchronized (lock2) {
      cost = 0;
    }
  }

  public void plusRequest() {
    synchronized (lock0) {
      request++;
    }
  }

  public void plusFinished() {
    synchronized (lock1) {
      finished++;
    }
  }

  public void plusCost(long n) {
    synchronized (lock2) {
      cost = cost + n;
    }
  }

  public long getRequest() {
    synchronized (lock0) {
      return request;
    }
  }

  public long getFinished() {
    synchronized (lock1) {
      totalFinished = totalFinished + finished;
      return finished;
    }
  }

  public long getCost() {
    synchronized (lock2) {
      totalCost = totalCost + cost;
      return cost;
    }
  }

  public long getTotalFinished() {
    synchronized (lock1) {
      return totalFinished;
    }
  }

  public long getTotalCost() {
    synchronized (lock2) {
      return totalCost;
    }
  }


  private final Object lock_state = new Object();

  private volatile ProviderStateEnum stateEnum = ProviderStateEnum.NORMAL;

  public void setState(double curr, double avg) {
    ProviderStateEnum tmp = state(curr, avg);
    synchronized (lock_state) {
      this.stateEnum = tmp;
    }
  }

  public ProviderStateEnum getStateEnum() {
      return stateEnum;
  }

  private ProviderStateEnum state(double c, double avg) {
    if (c > avg) {
      if ((c - avg) / avg > 0.35) {
        return ProviderStateEnum.$6BUSY;
      }
      if ((c - avg) / avg > 0.3) {
        return ProviderStateEnum.$5BUSY;
      }
      if ((c - avg) / avg > 0.25) {
        return ProviderStateEnum.$4BUSY;
      }
      if ((c - avg) / avg > 0.2) {
        return ProviderStateEnum.$3BUSY;
      }
      if ((c - avg) / avg > 0.15) {
        return ProviderStateEnum.$2BUSY;
      }
      if ((c - avg) / avg > 0.1) {
        return ProviderStateEnum.$1BUSY;
      }
      if ((c - avg) / avg > 0.05) {
        return ProviderStateEnum.BUSY;
      }
    }

    if (c < avg) {
      if ((avg - c) / c > 0.1) {
        return ProviderStateEnum.$IDLE;
      }
      if ((avg - c) / c > 0.05) {
        return ProviderStateEnum.IDLE;
      }
    }

    return ProviderStateEnum.NORMAL;
  }

  enum ProviderStateEnum {
    $IDLE(3),
    IDLE(1),
    NORMAL(0),
    BUSY(-1),
    $1BUSY(-2),
    $2BUSY(-3),
    $3BUSY(-4),
    $4BUSY(-5),
    $5BUSY(-6),
    $6BUSY(-7),
    ;

    private double value;

    ProviderStateEnum(double value) {
      this.value = value;
    }

    public double getValue() {
      return value;
    }

    public boolean isBusy() {
      return value < 0;
    }
  }


  private volatile int max = Integer.MAX_VALUE;

  private volatile int active = 0;

  private final Object lock_t = new Object();

  public void setMax(int max, int active) {
    synchronized (lock_t) {
      this.max = max;
      this.active = active;
    }
  }

  public boolean tooHot() {
    synchronized (lock_t) {
      return (max - active) < 4;
    }
  }
}
