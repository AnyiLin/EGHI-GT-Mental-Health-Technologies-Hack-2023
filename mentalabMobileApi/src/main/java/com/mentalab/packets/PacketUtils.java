package com.mentalab.packets;

import com.mentalab.exception.InvalidDataException;
import com.mentalab.utils.constants.SamplingRate;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketUtils {

  private static final int BYTES_PER_DOUBLE = 2;
  private static final int BUFFER_LENGTH = 3; // EEG packets are 24 bits = 3 bytes

  public static int bytesToInt(byte... data) throws InvalidDataException {
    final byte[] array = bytesToArray(4, data);
    return ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }

  public static short bytesToShort(byte... data) throws InvalidDataException {
    final byte[] array = bytesToArray(2, data);
    return ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).getShort();
  }

  public static double bytesToDouble(byte... data) throws InvalidDataException {
    final byte[] array = bytesToArray(4, data);
    return ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).getDouble();
  }

  public static float bytesToFloat(byte... data) throws InvalidDataException {
    final byte[] array = bytesToArray(4, data);
    return ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).getFloat();
  }

  public static double[] bytesToDoubles(byte[] data) throws InvalidDataException {
    if (data.length % BYTES_PER_DOUBLE != 0) {
      throw new InvalidDataException("Illegal byte array length");
    }

    final int arraySize = data.length / BYTES_PER_DOUBLE;
    final double[] array = new double[arraySize]; // zeros
    return addValsToArrayDouble(data, array);
  }

  public static double[] bytesToInt32s(byte[] data) throws InvalidDataException {
    if (data.length % BUFFER_LENGTH != 0) {
      throw new InvalidDataException("Illegal byte array length");
    }

    int arraySize = data.length / BUFFER_LENGTH;
    double[] array = new double[arraySize];
    return addValsToArray32(data, array);
  }

  private static byte[] bytesToArray(int arrayLength, byte... data) throws InvalidDataException {
    if (data.length > arrayLength) {
      throw new InvalidDataException("Illegal byte array length");
    }
    final byte[] array = new byte[arrayLength]; // zeros
    return addValsToArray(data, array);
  }

  private static byte[] addValsToArray(byte[] data, byte[] array) {
    // fill-in with our data
    System.arraycopy(data, 0, array, 0, data.length);
    return array;
  }

  private static double[] addValsToArrayDouble(byte[] data, double[] array)
      throws InvalidDataException {
    for (int i = 0; i < data.length; i += BYTES_PER_DOUBLE) {
      array[i / BYTES_PER_DOUBLE] = bytesToShort(data[i], data[i + 1]);
    }
    return array;
  }

  // todo: simplify this
  private static double[] addValsToArray32(byte[] data, double[] array)
      throws InvalidDataException {
    for (int i = 3; i < data.length; i += 3) { // skip first byte because 0 is the ads mask
      final int bitSign = data[i + 2] >> 7;
      if (bitSign == 0) {
        array[i / 3] = PacketUtils.bytesToInt(data[i], data[i + 1], data[i + 2]);
      } else {
        int twosComplimentValue = PacketUtils.bytesToInt(data[i], data[i + 1], data[i + 2]);
        array[i / 3] = -1 * (Math.pow(2, 24) - twosComplimentValue);
      }
    }
    return array;
  }

  public static SamplingRate adsCodeToSamplingRate(int samplingRateCode)
      throws InvalidDataException {
    for (SamplingRate sr : SamplingRate.values()) {
      if (sr.getAdsCode() == samplingRateCode) {
        return sr;
      }
    }
    throw new InvalidDataException("Cannot decipher sampling rate from DeviceInfoPacket.");
  }
}
