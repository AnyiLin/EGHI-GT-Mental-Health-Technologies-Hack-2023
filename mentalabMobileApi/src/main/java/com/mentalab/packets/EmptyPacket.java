package com.mentalab.packets;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.utils.constants.Topic;

public class EmptyPacket extends Packet {

  public EmptyPacket(double timeStamp) {
    super(timeStamp);
  }

  @Override
  public void populate(byte[] byteBuffer) throws InvalidDataException {
    // ignored
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: Empty";
  }

  @Override
  public Topic getTopic() {
    return null;
  }
}
