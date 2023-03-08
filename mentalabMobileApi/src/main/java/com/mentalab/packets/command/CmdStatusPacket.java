package com.mentalab.packets.command;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;

public class CmdStatusPacket extends Packet {

  private boolean commandStatus;

  public CmdStatusPacket(double timeStamp) {
    super(timeStamp);
  }

  @Override
  public void populate(byte[] data) throws InvalidDataException {
    short status = PacketUtils.bytesToShort(data[5]);
    this.commandStatus = status != 0;
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: CommandStatus";
  }

  @Override
  public int getDataCount() {
    return 1;
  }

  public boolean getResult() {
    return commandStatus;
  }

  @Override
  public Topic getTopic() {
    return Topic.COMMAND;
  }
}
