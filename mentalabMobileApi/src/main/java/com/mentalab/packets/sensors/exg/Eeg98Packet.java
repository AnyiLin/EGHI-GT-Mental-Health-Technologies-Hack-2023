package com.mentalab.packets.sensors.exg;

import static com.mentalab.packets.PacketDataType.CH1;
import static com.mentalab.packets.PacketDataType.CH8;

import java.util.EnumSet;

public class Eeg98Packet extends EEGPacket {

  private static final int NO_CHANNELS = 8;

  public Eeg98Packet(double timeStamp) {
    super(timeStamp, NO_CHANNELS);
    super.type = EnumSet.range(CH1, CH8);
  }
}
