package com.mentalab.packets.sensors;

import static com.mentalab.packets.PacketDataType.BATTERY;
import static com.mentalab.packets.PacketDataType.LIGHT;
import static com.mentalab.packets.PacketDataType.TEMP;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;
import java.util.EnumSet;

public class EnvironmentPacket extends Packet {

  private static final double LUX_CONSTANT = 0.2442002442002442; // rounded to ~52-bit Mantissa
  private static final double BATTERY_CONSTANT = 0.0018099547511312; // rounded

  public EnvironmentPacket(double timeStamp) {
    super(timeStamp);
    super.type = EnumSet.of(TEMP, LIGHT, BATTERY);
  }

  private static double getRawBattery(byte[] byteBuffer) throws InvalidDataException {
    return PacketUtils.bytesToInt(byteBuffer[3], byteBuffer[4]) * BATTERY_CONSTANT;
  }

  private static double getBatteryPercentage(double voltage) {
    if (voltage < 3.1) {
      return 1d;
    } else if (voltage < 3.5) {
      return 1 + (voltage - 3.1) / .4 * 10;
    } else if (voltage < 3.8) {
      return 10d + (voltage - 3.5) / .3 * 40d;
    } else if (voltage < 3.9) {
      return 40d + (voltage - 3.8) / .1 * 20d;
    } else if (voltage < 4) {
      return 60d + (voltage - 3.9) / .1 * 15d;
    } else if (voltage < 4.1) {
      return 75d + (voltage - 4.0) / .1 * 15d;
    } else if (voltage < 4.2) {
      return 90d + (voltage - 4.1) / .1 * 10d;
    } else {
      return 100d;
    }
  }

  @Override
  public void populate(byte[] data) throws InvalidDataException {
    super.data.add((float) PacketUtils.bytesToInt(data[0])); // temp
    super.data.add((float) (PacketUtils.bytesToInt(data[1], data[2]) * LUX_CONSTANT)); // light
    super.data.add((float) getBatteryPercentage(getRawBattery(data))); // battery
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: Environment";
  }

  @Override
  public int getDataCount() {
    return super.type.size();
  }

  @Override
  public Topic getTopic() {
    return Topic.ENVIRONMENT;
  }
}
