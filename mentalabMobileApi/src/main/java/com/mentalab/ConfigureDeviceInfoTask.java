package com.mentalab;

import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.packets.info.DeviceInfoPacket;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.DeviceInfoSubscriber;
import com.mentalab.utils.CheckedExceptionSupplier;
import java.io.IOException;

public class ConfigureDeviceInfoTask implements CheckedExceptionSupplier<Boolean> {

  private final ExploreDevice device;

  ConfigureDeviceInfoTask(ExploreDevice device) {
    this.device = device;
  }

  private static DeviceInfoSubscriber registerSubscriber() {
    final DeviceInfoSubscriber sub = new DeviceInfoSubscriber();
    ContentServer.getInstance().registerSubscriber(sub);
    return sub;
  }

  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   * @throws InterruptedException when calling process timeout forces return from packet subscriber
   */
  @Override
  public Boolean accept()
      throws InterruptedException, InvalidCommandException, IOException, NoBluetoothException {
    final DeviceInfoSubscriber sub = registerSubscriber();
    final DeviceInfoPacket deviceInfo = sub.awaitResult();
    ContentServer.getInstance().deRegisterSubscriber(sub);
    return configureExploreDevice(deviceInfo);
  }

  private Boolean configureExploreDevice(DeviceInfoPacket deviceInfo) {
    this.device.setSR(deviceInfo.getSamplingRate());
    this.device.setChannelMask(deviceInfo.getChannelMask());
    return true;
  }
}
