package com.mentalab.packets.sensors;

import static com.mentalab.packets.PacketDataType.ACCX;
import static com.mentalab.packets.PacketDataType.GYROZ;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;
import java.util.EnumSet;

public class OrientationPacket extends Packet {

  private static final double ACCELEROMETER_CONSTANT = 0.061;
  private static final double GYROSCOPE_CONSTANT = 8.750;
  private static final double MAGNETOMETER_CONSTANT = 1.52;

  public OrientationPacket(double timeStamp) {
    super(timeStamp);
    super.type = EnumSet.range(ACCX, GYROZ);
  }

  @Override
  public void populate(byte[] dataBytes) throws InvalidDataException {
    final double[] dataDoubles = PacketUtils.bytesToDoubles(dataBytes);
    for (int i = 0; i < dataDoubles.length; i++) {
      if (i < 3) {
        super.data.add((float) (dataDoubles[i] * ACCELEROMETER_CONSTANT));
      } else if (i < 6) {
        super.data.add((float) (dataDoubles[i] * GYROSCOPE_CONSTANT));
      } else if (i == 6) {
        super.data.add((float) (dataDoubles[i] * MAGNETOMETER_CONSTANT * -1));
      } else {
        super.data.add((float) (dataDoubles[i] * MAGNETOMETER_CONSTANT));
      }
    }
  }

  @NonNull
  @Override
  public String toString() {
    return "PACKET: Orientation";
  }

  @Override
  public int getDataCount() {
    return super.type.size();
  }

  @Override
  public Topic getTopic() {
    return Topic.ORN;
  }
}
