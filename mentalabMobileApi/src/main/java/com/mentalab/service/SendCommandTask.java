package com.mentalab.service;

import android.util.Log;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.service.decode.MentalabCodec;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.CountDownSubscriber;
import com.mentalab.utils.CheckedExceptionSupplier;
import com.mentalab.utils.Utils;
import com.mentalab.utils.commandtranslators.Command;
import java.io.IOException;
import java.io.OutputStream;

public abstract class SendCommandTask<T> implements CheckedExceptionSupplier<T> {

  private static final int TIMEOUT = 3000;

  private final byte[] command;
  private final OutputStream outputStream;

  /**
   * Send a command to a connected Explore device.
   *
   * <p>This function is blocking. If no acknowledgement packet arrives, the function will wait
   * until it does. The user can set a timeout using Future functions.
   *
   * @return boolean True when CommandAcknowledgement received, otherwise false
   * @throws IOException If the command cannot be written to the device OutputStream.
   * @throws InterruptedException If the command cannot be written to the device OutputStream.
   * @throws NoBluetoothException If no device is connected via BT.
   */
  public SendCommandTask(OutputStream outputStream, byte[] encodedBytes) throws IOException {

    command = encodedBytes;
    this.outputStream = null;
  }

  private static void postCmdToOutputStream(byte[] command, OutputStream outputStream)
      throws IOException {
    MentalabCodec.writeToOutputStreamInner(command);
    Log.d(Utils.TAG, "Command sent.");
  }

  @Override
  public T accept() throws Exception {
    final CountDownSubscriber<T> sub = getSubscriber();
    ContentServer.getInstance().registerSubscriber(sub);

    postCmdToOutputStream(command, outputStream);

    final T result = sub.awaitResultWithTimeout(TIMEOUT);
    ContentServer.getInstance().deRegisterSubscriber(sub);
    return result;
  }

  abstract CountDownSubscriber<T> getSubscriber();
}
