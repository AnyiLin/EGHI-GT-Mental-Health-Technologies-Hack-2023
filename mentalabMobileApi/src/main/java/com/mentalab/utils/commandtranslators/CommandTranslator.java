package com.mentalab.utils.commandtranslators;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class CommandTranslator {

  private static final int[] FLETCHER_BYTES = new int[] {0xAF, 0xBE, 0xAD, 0xDE};

  int pId;
  int count = 0x00;
  int hostTimestamp;
  int opcode;
  int arg;
  int payload;
  int dataLength;

  public abstract byte[] translateCommand();

  byte[] convertIntegerToByteArray() {
    byte[] convertedData = new byte[dataLength];
    convertedData[0] = (byte) pId;
    convertedData[1] = (byte) count;
    convertedData[2] = (byte) payload;
    convertedData[3] = (byte) 0;

    ByteBuffer timestampBuffer = ByteBuffer.allocate(4);
    timestampBuffer.order(ByteOrder.LITTLE_ENDIAN);
    timestampBuffer.putInt(hostTimestamp);
    byte[] byteArray = timestampBuffer.array();
    int i;
    for (i = 0; i < 4; i++) {
      convertedData[i + 4] = byteArray[i];
    }

    i = i + 4;

    convertedData[i++] = (byte) opcode;

    convertedData[i++] = (byte) arg;
    for (int fletcherArrayIndex = 0; fletcherArrayIndex < 4; fletcherArrayIndex++) {
      convertedData[fletcherArrayIndex + i] = (byte) FLETCHER_BYTES[fletcherArrayIndex];
    }

    return convertedData;
  }
}
