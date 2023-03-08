package com.mentalab.packets.info;


import static com.mentalab.packets.PacketDataType.ADS_MASK;
import static com.mentalab.packets.PacketDataType.SR;

import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.PacketUtils;
import java.util.EnumSet;

/** Device related information packet to transmit firmware version, ADC mask and sampling rate */
public class DeviceInfoPacketV1 extends DeviceInfoPacket {

  public DeviceInfoPacketV1(double timeStamp) {
    super(timeStamp);
    super.type = EnumSet.of(ADS_MASK, SR);
  }

  @Override
  public void populate(byte[] data) throws InvalidDataException {
    final int adsSamplingRateCode = PacketUtils.bytesToInt(data[2]); // 4, 5, or 6
    this.samplingRate = PacketUtils.adsCodeToSamplingRate(adsSamplingRateCode);
    this.adsMask = data[3] & 0xFF;
  }

  @Override
  public int getDataCount() {
    return 2;
  }
}
