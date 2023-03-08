package com.mentalab.packets.sensors;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketDataType;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;
import java.util.EnumSet;

public class MarkerPacket extends Packet {

  public MarkerPacket(double timeStamp) {
    super(timeStamp);
    super.type = EnumSet.of(PacketDataType.MARKER);
  }

  @Override
  public void populate(byte[] data) throws InvalidDataException {
    final int markerCode = PacketUtils.bytesToShort(data[0]);
    super.data.add((float) markerCode);
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: Marker";
  }

  @Override
  public int getDataCount() {
    return 1;
  }

  public Topic getTopic() {
    return Topic.MARKER;
  }
}
