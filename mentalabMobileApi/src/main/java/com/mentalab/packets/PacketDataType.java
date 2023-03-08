package com.mentalab.packets;

import androidx.annotation.NonNull;

public enum PacketDataType {
  ADS_MASK("Ads Mask"),
  SR("Sampling Rate"),
  BOARD_ID("Board ID"),
  MEMORY_INFO("Memory info"),

  TEMP("Temperature"),
  LIGHT("Light"),
  BATTERY("Battery"),

  SLOPE("Slope"),
  OFFSET("Offset"),

  MARKER("Marker"),

  ACCX("Accelerometer X"),
  ACCY("Accelerometer Y"),
  ACCZ("Accelerometer Z"),
  MAGX("Magnetometer X"),
  MAGY("Magnetometer Y"),
  MAGZ("Magnetometer Z"),
  GYROX("Gyroscope X"),
  GYROY("Gyroscope Y"),
  GYROZ("Gyroscope Z"),

  CH1("Channel 1"),
  CH2("Channel 2"),
  CH3("Channel 3"),
  CH4("Channel 4"),
  CH5("Channel 5"),
  CH6("Channel 6"),
  CH7("Channel 7"),
  CH8("Channel 8"),
  CH9("Channel 9"),
  CH10("Channel 10"),
  CH11("Channel 11"),
  CH12("Channel 12"),
  CH13("Channel 13"),
  CH14("Channel 14"),
  CH15("Channel 15"),
  CH16("Channel 16"),
  CH17("Channel 17"),
  CH18("Channel 18"),
  CH19("Channel 19"),
  CH20("Channel 20"),
  CH21("Channel 21"),
  CH22("Channel 22"),
  CH23("Channel 23"),
  CH24("Channel 24"),
  CH25("Channel 25"),
  CH26("Channel 26"),
  CH27("Channel 27"),
  CH28("Channel 28"),
  CH29("Channel 29"),
  CH30("Channel 30"),
  CH31("Channel 31"),
  CH32("Channel 32");

  private final String type;

  PacketDataType(String s) {
    this.type = s;
  }

  @NonNull
  public String toString() {
    return this.type;
  }
}
