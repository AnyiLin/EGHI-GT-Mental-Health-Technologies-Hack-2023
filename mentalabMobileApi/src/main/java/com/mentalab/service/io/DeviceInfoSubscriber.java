package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.packets.info.DeviceInfoPacket;
import com.mentalab.utils.constants.Topic;

public class DeviceInfoSubscriber extends CountDownSubscriber<DeviceInfoPacket> {

  public DeviceInfoSubscriber() {
    super(Topic.DEVICE_INFO);
  }

  @Override
  public void accept(Packet packet) {
    result = (DeviceInfoPacket) packet;
    latch.countDown();
  }
}
