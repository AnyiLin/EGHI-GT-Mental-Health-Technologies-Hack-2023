package com.mentalab.utils.commandtranslators;

import java.sql.Timestamp;

public class TwoByteCommandTranslator extends CommandTranslator {

  private static final int TWO_BYTE_COMMAND = 0xA0;

  public TwoByteCommandTranslator(int opCode, int argument) {
    this.pId = TWO_BYTE_COMMAND;
    this.payload = 10;
    this.hostTimestamp = new Timestamp(System.currentTimeMillis()).getNanos();
    this.opcode = opCode;
    this.arg = argument;
    this.dataLength = 14;
  }

  @Override
  public byte[] translateCommand() {
    return convertIntegerToByteArray();
  }
}
