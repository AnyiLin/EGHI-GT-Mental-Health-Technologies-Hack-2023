package com.mentalab;

import com.mentalab.service.io.ChannelCountSubscriber;
import com.mentalab.service.io.ContentServer;
import com.mentalab.utils.CheckedExceptionSupplier;
import com.mentalab.utils.Utils;
import com.mentalab.utils.constants.ChannelCount;

public class ConfigureChannelCountTask implements CheckedExceptionSupplier<Boolean> {

  private final ExploreDevice device;

  ConfigureChannelCountTask(ExploreDevice device) {
    this.device = device;
  }

  private static ChannelCountSubscriber registerSubscriber() {
    final ChannelCountSubscriber sub = new ChannelCountSubscriber();
    ContentServer.getInstance().registerSubscriber(sub);
    return sub;
  }

  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   * @throws InterruptedException when calling process timeout forces return from packet subscriber
   */
  @Override
  public Boolean accept() throws InterruptedException {
    final ChannelCountSubscriber sub = registerSubscriber();
    final int channelCount = sub.awaitResult();
    ContentServer.getInstance().deRegisterSubscriber(sub);
    return configureExploreDevice(channelCount);
  }

  private Boolean configureExploreDevice(int channelCount) {
    if (channelCount < 1) {
      return false;
    }
    final ChannelCount cc = Utils.getChannelCountFromInt(channelCount);
    this.device.setChannelCount(cc);
    return true;
  }
}
