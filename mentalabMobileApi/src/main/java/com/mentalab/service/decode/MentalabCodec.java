package com.mentalab.service.decode;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.mentalab.BluetoothManager;
import com.mentalab.ExploreDevice;
import com.mentalab.MentalabCommands;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketId;
import com.mentalab.service.io.ContentServer;
import com.mentalab.utils.Utils;
import com.mentalab.utils.commandtranslators.Command;
import com.mentalab.utils.commandtranslators.CommandTranslator;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MentalabCodec {

  // runs independently of all else
  private static final ExecutorService DECODE_EXECUTOR = Executors.newFixedThreadPool(5);
  private final ParserInner DECODER_TASK = new ParserInner();
  static final BluetoothSocket mmsocket = BluetoothManager.getBtSocket();
  private static String deviceName;
  private static ExploreDevice device;

  private MentalabCodec() {}

  public static MentalabCodec getInstance() {
    return MentalabCodec.InstanceHolder.INSTANCE;
  }

  /**
   * Encodes a command
   *
   * @return byte[] encoded commands that can be sent to the device
   */
  public static byte[] encodeCommand(Command command) {
    final CommandTranslator translator = command.createCommandTranslator();
    return translator.translateCommand();
  }

  public static void writeToOutputStreamInner(byte[] bytes) throws IOException {

    synchronized (mmsocket){
      OutputStream os = mmsocket.getOutputStream();
      os.write(bytes);
      os.flush();
    }
  }

  /**
   * Tells the ExploreExecutor to decode the raw data.
   *
   * @param rawData InputStream of device bytes
   */
  public void decodeInputStream(InputStream rawData, ExploreDevice exploreDevice) {
    device = exploreDevice;
    DECODER_TASK.setInputStream(rawData);
    DECODE_EXECUTOR.submit(DECODER_TASK);
  }

  public void shutdown() {
    Thread.currentThread().interrupt();
    DECODE_EXECUTOR.shutdownNow();
  }



  private static class InstanceHolder { // Initialization-on-demand synchronization
    private static final MentalabCodec INSTANCE = new MentalabCodec();
  }

  private static class ParserInner extends Thread {

    private BufferedInputStream btInputStream;

    private int readToInt(InputStream i, int noBytesToRead) throws IOException {
      final byte[] buffer = readStream(i, noBytesToRead, 1024);
      return ByteBuffer.wrap(buffer).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private double readToDouble(InputStream i, int noBytesToRead) throws IOException {
      final byte[] buffer = readStream(i, noBytesToRead, 1024);
      return ByteBuffer.wrap(buffer).order(java.nio.ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    private byte[] readStream(InputStream i, int noBytesToRead, int initialBufferLength)
        throws IOException {
      final byte[] buffer = new byte[initialBufferLength];
      int read = i.read(buffer, 0, noBytesToRead); // read into buffer
      if (read < noBytesToRead) {
        Log.e(Utils.TAG, "Not all payload data read into buffer");
      }
      return buffer;
    }

    private Packet parsePayload(byte[] bufferedData, int pId, double timeStamp)
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

    private PacketId getPacketId(int pId) throws InvalidDataException {
      for (PacketId p : PacketId.values()) {
        if (pId == p.getNumVal()) {
          return p;
        }
      }
      throw new InvalidDataException("Cannot identify packet type.");
    }

    void setInputStream(InputStream inputStream) {
      this.btInputStream = new BufferedInputStream(inputStream, 10 *1024);
    }

    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          synchronized (mmsocket) {
            final int pID = readToInt(btInputStream, 1); // package identification
            final int count = readToInt(btInputStream, 1); // package count
            final int length =
                readToInt(btInputStream, 2); // bytes = timestamp + payload + fletcher
            final double timeStamp = readToInt(btInputStream, 4); // in ms * 10
            final Packet packet = createPacket(pID, length, timeStamp / 10_000); // to seconds
            if(pID != 177) ContentServer.getInstance().publish(packet.getTopic(), packet);
            }

        } catch (IOException e) {
          if(Thread.currentThread().isInterrupted())
          {
            Log.d(Utils.TAG, "Shutdown called. Parser will exit", e);
          }
          else
          {
            Log.e(Utils.TAG, "Error reading input stream. Exiting.", e);
            try {
              MentalabCommands.connect(MentalabCodec.device.getDeviceName());
              MentalabCodec.device = new ExploreDevice(MentalabCodec.device);;
              MentalabCodec.device.acquire();
            } catch (NoBluetoothException | NoConnectionException | IOException | ExecutionException | InterruptedException ex) {
              ex.printStackTrace();
            }
          }
          break;
        }
      }
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

  public static ExploreDevice getLastConnectedDevice(){
      return device;
  }
}
