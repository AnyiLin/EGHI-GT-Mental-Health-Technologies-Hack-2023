package com.mentalab.service.io;

import com.mentalab.utils.constants.Topic;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class CountDownSubscriber<T> extends Subscriber<T> {

  final CountDownLatch latch = new CountDownLatch(1);
  volatile T result;

  public CountDownSubscriber(Topic t) {
    super(t);
  }

  public T awaitResultWithTimeout(int timeout) throws InterruptedException {
    latch.await(timeout, TimeUnit.MILLISECONDS);
    return result;
  }

  public T awaitResult() throws InterruptedException {
    latch.await();
    return result;
  }
}
