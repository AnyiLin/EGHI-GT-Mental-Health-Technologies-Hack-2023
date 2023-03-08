package com.mentalab.utils;

import com.mentalab.utils.constants.ConfigProtocol;

public class ConfigSwitch {

  private final ConfigProtocol configProtocol;
  private final boolean on;

  public ConfigSwitch(ConfigProtocol p, boolean turnOn) {
    this.configProtocol = p;
    this.on = turnOn;
  }

  public boolean isOn() {
    return on;
  }

  public ConfigProtocol getProtocol() {
    return configProtocol;
  }
}
