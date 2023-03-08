package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.utils.constants.Topic;

public class ChannelCountSubscriber extends CountDownSubscriber<Integer> {

  public ChannelCountSubscriber() {
    super(Topic.EXG);
  }

  @Override
  public void accept(Packet packet) {
    result = packet.getDataCount();
    latch.countDown();
  }
}
