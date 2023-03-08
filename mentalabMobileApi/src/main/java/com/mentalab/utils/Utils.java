package com.mentalab.utils;

import android.util.Log;
import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.utils.constants.ChannelCount;
import com.mentalab.utils.constants.ConfigProtocol;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

  public static final String TAG = "Explore";
  public static final DecimalFormat DF = new DecimalFormat("#.####");

  public static String checkName(String deviceName) throws NoConnectionException {
    deviceName = tryAppendWithExplore(deviceName);
    checkNameStartsWithExplore(deviceName);
    return deviceName;
  }

  private static void checkNameStartsWithExplore(String deviceName) throws NoConnectionException {
    if (!deviceName.startsWith("Explore_")) {
      throw new NoConnectionException(
          "Device names must begin with 'Explore_'. Provided device name: '"
              + deviceName
              + "'. Exiting.");
    }
  }

  private static String tryAppendWithExplore(String deviceName) {
    if (deviceName.length() == 4) {
      Log.i(TAG, "Appending device name with 'Explore_'.");
      deviceName = "Explore_" + deviceName;
    }
    return deviceName;
  }

  public static void checkSwitchTypes(Set<ConfigSwitch> switches, ConfigProtocol.Type type)
      throws InvalidCommandException {
    if (!switches.stream().allMatch(s -> s.getProtocol().isOfType(type))) {
      throw new InvalidCommandException(invalidSwitchString(type));
    }
  }

  public static void checkSwitchType(ConfigSwitch switchI, ConfigProtocol.Type type)
      throws InvalidCommandException {
    if (!switchI.getProtocol().isOfType(type)) {
      throw new InvalidCommandException(invalidSwitchString(type));
    }
  }

  private static String invalidSwitchString(ConfigProtocol.Type falseType) {
    return "Attempting to send a command using an invalid switch of type: "
        + falseType
        + ". Exiting.";
  }

  public static String round(double d) {
    DF.setRoundingMode(RoundingMode.FLOOR);
    return DF.format(d);
  }

  public static ChannelCount getChannelCountFromInt(int i) {
    if (i < 5) {
      return ChannelCount.CC_4;
    }
    if(i < 9) {
      return ChannelCount.CC_8;
    }
    return ChannelCount.CC_32;
  }

  public static Set<ConfigSwitch> removeRedundantSwitches(
      Set<ConfigSwitch> switches, ChannelCount channelCount) {
    return switches.stream()
        .filter(s -> s.getProtocol().getID() < channelCount.getAsInt())
        .collect(Collectors.toSet());
  }

  /** 6 -> 0110 instead of 110 */
  public static String intToBinaryString(int i, ChannelCount channelCount) {
    return String.format("%" + channelCount.getAsInt() + "s", Integer.toBinaryString(i))
        .replace(' ', '0');
  }
}
