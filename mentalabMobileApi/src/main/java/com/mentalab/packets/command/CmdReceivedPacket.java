package com.mentalab.packets.command;

import androidx.annotation.NonNull;
import com.mentalab.packets.Packet;
import com.mentalab.utils.constants.Topic;

public class CmdReceivedPacket extends Packet {

  public CmdReceivedPacket(double timeStamp) {
    super(timeStamp);
  }

  @Override
  public void populate(byte[] data) {
    // ignored
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: CommandReceived";
  }

  @Override
  public int getDataCount() {
    return 1;
  }

  @Override
  public Topic getTopic() {
    return Topic.COMMAND;
  }
}
