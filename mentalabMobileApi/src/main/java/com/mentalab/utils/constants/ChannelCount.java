package com.mentalab.utils.constants;

public enum ChannelCount {
  CC_4(4, 33),
  CC_8(8, 16),
  CC_32(32, 4); // TODO Note that this may be 5 in the future

  private final int integerRepresentation;
  private final int sampleSize;

  ChannelCount(int i, int sampleSize) {
    this.integerRepresentation = i;
    this.sampleSize = sampleSize;
  }

  public int getAsInt() {
    return integerRepresentation;
  }

  public int getSampleSize() {
    return sampleSize;
  }
}
