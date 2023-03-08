package com.mentalab;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ExploreExecutor {

  final AtomicBoolean isLocked = new AtomicBoolean(true);

  private ExecutorService serialExecutor = Executors.newSingleThreadExecutor();
  private ExecutorService parallelExecutor = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

  private ExploreExecutor() {}

  public static ExploreExecutor getInstance() {
    return ExploreExecutor.InstanceHolder.INSTANCE;
  }

  ExecutorService getExecutor() throws RejectedExecutionException {
    checkAvailablity();
    return this.parallelExecutor;
  }

  ExecutorService getSerialExecutor() throws RejectedExecutionException {
    checkAvailablity();
    return this.serialExecutor;
  }

  ScheduledExecutorService getScheduledExecutor() throws RejectedExecutionException {
    checkAvailablity();
    return this.scheduledExecutor;
  }

  void resetExecutorServices() {
    shutDownNow();
    this.serialExecutor = Executors.newSingleThreadExecutor();
    this.parallelExecutor = Executors.newFixedThreadPool(5);
    this.scheduledExecutor = Executors.newScheduledThreadPool(2);
  }

  void shutDownNow() {
    this.serialExecutor.shutdownNow();
    this.parallelExecutor.shutdownNow();
    this.scheduledExecutor.shutdownNow();
  }

  void shutDown() {
    this.serialExecutor.shutdown();
    this.parallelExecutor.shutdown();
    this.scheduledExecutor.shutdown();
  }

  /**
   * Checks to see whether the AtomicBoolean is set to false. If so, throws
   * RejectedExecutionException
   */
  private synchronized void checkAvailablity() throws RejectedExecutionException {
    if (!isLocked.get()) {
      this.serialExecutor.shutdownNow();
      this.serialExecutor = Executors.newSingleThreadExecutor();
    }
  }

  public AtomicBoolean getLock() {
    return isLocked;
  }

  private static class InstanceHolder { // Initialization-on-demand synchronization
    private static final ExploreExecutor INSTANCE = new ExploreExecutor();
  }
}
