package com.mentalab.packets.sensors.exg;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;
import java.io.IOException;

public abstract class EEGPacket extends Packet {

  private final int noChannels;

  public EEGPacket(double timeStamp, int noChannels) {
    super(timeStamp);
    this.noChannels = noChannels;
  }

  private static Float adjustGain(double dataPoint) {
    final double exgUnit = Math.pow(10, -6);
    final double vRef = 2.4;
    final double gain = (exgUnit * (Math.pow(2, 23) - 1)) * 6;
    return (float) (dataPoint * (vRef / gain));
  }

  @Override
  public void populate(byte[] dataBytes) throws InvalidDataException, IOException {
    double[] dataDoubles = PacketUtils.bytesToInt32s(dataBytes);
    for (int i = 0; i < dataDoubles.length; i++) {
      if (i % (noChannels + 1) == 0) {
        continue; // skip status bit
      }
      super.data.add(adjustGain(dataDoubles[i]));
    }
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: ExG";
  }

  @Override
  public int getDataCount() {
    return this.noChannels;
  }

  @Override
  public Topic getTopic() {
    return Topic.EXG;
  }
}
