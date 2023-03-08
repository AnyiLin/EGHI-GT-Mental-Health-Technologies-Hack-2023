package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.packets.command.CmdReceivedPacket;
import com.mentalab.packets.command.CmdStatusPacket;
import com.mentalab.utils.constants.Topic;

public class CommandAcknowledgeSubscriber extends CountDownSubscriber<Boolean> {

  public CommandAcknowledgeSubscriber() {
    super(Topic.COMMAND);
  }

  @Override
  public void accept(Packet p) {
    if (p instanceof CmdReceivedPacket) {
      return; // no need to do anything. Await status.
    }

    if (p instanceof CmdStatusPacket) {
      result = ((CmdStatusPacket) p).getResult();
    } else {
      result = false;
    }

    latch.countDown();
  }
}
