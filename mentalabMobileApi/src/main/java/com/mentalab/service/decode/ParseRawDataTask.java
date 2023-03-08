package com.mentalab.service.decode;

import android.util.Log;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketId;
import com.mentalab.service.io.ContentServer;
import com.mentalab.utils.Utils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Callable;

class ParseRawDataTask implements Callable<Void> {

  private BufferedInputStream btInputStream;

  private static int readToInt(InputStream i, int noBytesToRead) throws IOException {
    final byte[] buffer = readStream(i, noBytesToRead, 1024);
    return ByteBuffer.wrap(buffer).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
  }

  private static double readToDouble(InputStream i, int noBytesToRead) throws IOException {
    final byte[] buffer = readStream(i, noBytesToRead, 1024);
    return ByteBuffer.wrap(buffer).order(java.nio.ByteOrder.LITTLE_ENDIAN).getDouble();
  }

  private static byte[] readStream(InputStream i, int noBytesToRead, int initialBufferLength)
      throws IOException {
    final byte[] buffer = new byte[initialBufferLength];
    int read = i.read(buffer, 0, noBytesToRead); // read into buffer
    if (read < noBytesToRead) {
      Log.e(Utils.TAG, "Not all payload data read into buffer");
    }
    return buffer;
  }

  private static Packet parsePayload(byte[] bufferedData, int pId, double timeStamp)
      throws IOException {
    try {
      final Packet packet = getPacketId(pId).createInstance(timeStamp);
      packet.populate(bufferedData);
      return packet;
    } catch (InvalidDataException e) {
      Log.e(Utils.TAG, "Error parsing payload: ", e);
      return null;
    }
  }

  private static PacketId getPacketId(int pId) throws InvalidDataException {
    for (PacketId p : PacketId.values()) {
      if (pId == p.getNumVal()) {
        return p;
      }
    }
    throw new InvalidDataException("Cannot identify packet type.");
  }

  void setInputStream(InputStream inputStream) {
    this.btInputStream = new BufferedInputStream(inputStream);
  }

  public Void call() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        final int pID = readToInt(btInputStream, 1); // package identification
        final int count = readToInt(btInputStream, 1); // package count
        final int length = readToInt(btInputStream, 2); // bytes = timestamp + payload + fletcher
        final double timeStamp = readToInt(btInputStream, 4); // in ms * 10

        final Packet packet = createPacket(pID, length, timeStamp / 10_000); // to seconds
        ContentServer.getInstance().publish(packet.getTopic(), packet);
      } catch (IOException e) {
        if(Thread.currentThread().isInterrupted())
        {
          Log.d(Utils.TAG, "Shutdown called. Parser will exit", e);
        }
        else
        {
          Log.e(Utils.TAG, "Error reading input stream. Exiting.", e);
        }
        break;
      }
    }
    return null;
  }

  private Packet createPacket(int pID, int length, double timeStamp) throws IOException {
    final byte[] noFletcherBuffer = readStreamPayload(length);
    return parsePayload(noFletcherBuffer, pID, timeStamp);
  }

  private byte[] readStreamPayload(int length) throws IOException {
    final byte[] buffer =
        readStream(btInputStream, length - 4, length - 4); // already read timestamp
    return Arrays.copyOfRange(buffer, 0, buffer.length - 4); // ignore last 4 byte Fletcher
  }
}
