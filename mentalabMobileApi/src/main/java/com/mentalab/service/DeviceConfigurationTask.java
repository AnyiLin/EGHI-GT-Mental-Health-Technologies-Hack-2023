package com.mentalab.service;

import com.mentalab.service.io.CommandAcknowledgeSubscriber;
import com.mentalab.service.io.CountDownSubscriber;
import java.io.IOException;
import java.io.OutputStream;

public class DeviceConfigurationTask extends SendCommandTask<Boolean> {

  public DeviceConfigurationTask(OutputStream outputStream, byte[] encodedBytes)
      throws IOException {
    super(outputStream, encodedBytes);
  }

  @Override
  CountDownSubscriber<Boolean> getSubscriber() {
    return new CommandAcknowledgeSubscriber();
  }
}
