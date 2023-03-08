package com.mentalab.utils.constants;

public enum SamplingRate {
  SR_250(0x01, 250, 6),
  SR_500(0x02, 500, 5),
  SR_1000(0x03, 1000, 4);

  private final int binaryCode;
  private final int integerRepresentation;
  private final int adsCode; // ADS 1298 TI TexasInstruments specifies that, e.g., 4 = 1000 Hz

  SamplingRate(int b, int i, int c) {
    this.binaryCode = b;
    this.integerRepresentation = i;
    this.adsCode = c;
  }

  public int getCode() {
    return binaryCode;
  }

  public int getAsInt() {
    return integerRepresentation;
  }

  public int getAdsCode() {
    return adsCode;
  }
}
