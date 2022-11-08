package com.ooooo.juc;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SimulatedCAS {

  private int value;

  public SimulatedCAS(int value) {
    this.value = value;
  }

  public synchronized int getValue() {
    return value;
  }

  public synchronized boolean compareAndSet(int expectedValue, int newValue) {
    if (expectedValue == value) {
      this.value = newValue;
      return true;
    }
    return false;
  }
}
