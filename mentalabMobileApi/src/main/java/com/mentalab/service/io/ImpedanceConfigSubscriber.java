package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.packets.info.ImpedanceInfoPacket;
import com.mentalab.utils.constants.Topic;

public class ImpedanceConfigSubscriber extends CountDownSubscriber<ImpedanceInfoPacket> {

  public ImpedanceConfigSubscriber() {
    super(Topic.DEVICE_INFO);
  }

  @Override
  public void accept(Packet packet) {
    if (packet instanceof ImpedanceInfoPacket) {
      result = (ImpedanceInfoPacket) packet;
      latch.countDown();
    }
  }
}
