package com.aliware.tianchi;

/**
 * @author zrj CreateDate: 2019/5/25
 */
public class Context {

  private static Context instance = new Context();

  public static Context getInstance() {
    return instance;
  }

  private volatile int small = 200;

  private volatile int mid = 200;

  private volatile int large = 200;

  private final Object lock = new Object();

  private volatile ProviderStateEnum s = ProviderStateEnum.NORMAL;

  private volatile ProviderStateEnum m = ProviderStateEnum.NORMAL;

  private volatile ProviderStateEnum l = ProviderStateEnum.NORMAL;

  public ProviderStateEnum getS() {
    synchronized (lock) {
      return s;
    }
  }

  public ProviderStateEnum getM() {
    synchronized (lock) {
      return m;
    }
  }

  public ProviderStateEnum getL() {
    synchronized (lock) {
      return l;
    }
  }


  public void adjust(ProviderStateEnum s, Provider provider) {
//    synchronized (lock) {
      switch (provider) {
        case S: {
          if ((small > 20 && s.isBusy()) || (small < 500 && !s.isBusy())) {
            small += s.getValue();
          }
          this.s = s;
          break;
        }
        case M: {
          if ((mid > 20 && s.isBusy()) || (mid < 500 && !s.isBusy())) {
            mid += s.getValue();
          }
          this.m = s;
          break;
        }
        case L: {
          if ((large > 20 && s.isBusy()) || (large < 500 && !s.isBusy())) {
            large += s.getValue();
          }
          this.l = s;
          break;
        }
//      }
    }
  }

  public int totalWeight() {
    synchronized (lock) {
      return small + mid + large;
    }
  }

  public int small() {
//    synchronized (lock) {
    return small;
//    }
  }

  public int mid() {
//    synchronized (lock) {
    return mid;
//    }
  }

  public int large() {
//    synchronized (lock) {
    return large;
//    }
  }


  enum Provider {
    S,
    M,
    L,
    ;

  }
}
