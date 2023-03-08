package com.mentalab;

import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.packets.info.ImpedanceInfoPacket;
import com.mentalab.service.DeviceConfigurationTask;
import com.mentalab.service.ImpedanceConfigurationTask;
import com.mentalab.service.SendCommandTask;
import com.mentalab.service.decode.MentalabCodec;
import com.mentalab.utils.commandtranslators.Command;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class DeviceManager {

  static Future<Boolean> submitTask(Callable<Boolean> task) throws RejectedExecutionException {
    return ExploreExecutor.getInstance().getExecutor().submit(task);
  }

  static Future<Boolean> submitTimeoutTask(Callable<Boolean> task, int millis, Runnable cleanup)
      throws RejectedExecutionException {
    final Future<Boolean> handler =
        ExploreExecutor.getInstance().getScheduledExecutor().submit(task);
    ExploreExecutor.getInstance()
        .getScheduledExecutor()
        .schedule(
            () -> {
              handler.cancel(true);
              cleanup.run();
            },
            millis,
            TimeUnit.MILLISECONDS);
    return handler;
  }

  /**
   * Asynchronously submits a command to the OutputStream using task.
   *
   * @param task Task that will send command to the device.
   * @return Future<T>
   */
  static <T> CompletableFuture<T> submitCommand(SendCommandTask<T> task, T exceptionalReturn)
      throws RejectedExecutionException {
    return CompletableFuture.supplyAsync(task, ExploreExecutor.getInstance().getSerialExecutor())
        .exceptionally(e -> exceptionalReturn); // if throws an exception, return gracefully
  }

  /** Submit a command on a dedicated thread. */
  static <T> CompletableFuture<T> submitNewThreadCommand(
      SendCommandTask<T> task, T exceptionalReturn) {
    return CompletableFuture.supplyAsync(task, Executors.newSingleThreadExecutor())
        .exceptionally(e -> exceptionalReturn); // if throws an exception, return gracefully
  }

  /**
   * Asynchronously submits a command to the OutputStream using the DeviceConfigurationTask.
   *
   * @param c Command the command to be sent to the device.
   * @return Future True if the command was successfully received. Otherwise false
   * @throws InvalidCommandException If the command cannot be encoded.
   */
  static CompletableFuture<Boolean> submitConfigCommand(Command c)
      throws IOException, RejectedExecutionException, InvalidCommandException,
          NoBluetoothException {
    final byte[] encodedBytes = encodeCommand(c);
    final DeviceConfigurationTask task =
        new DeviceConfigurationTask(BluetoothManager.getOutputStream(), encodedBytes);
    if (c == Command.CMD_ZM_DISABLE) {
      // The impedance task blocks all threads. Stopping impedance requires a dedicated new thread.
      return submitNewThreadCommand(task, false);
    }
    return submitCommand(task, false);
  }

  /**
   * Asynchronously submits a command to the OutputStream using the DeviceConfigurationTask. If the
   * command was successful, run andThen.
   *
   * @param c Command to be sent to the device.
   * @param andThen Runnables to be completed *in order* only if the command is successfully
   *     received
   * @return Future True if the command was successfully received. Otherwise false
   * @throws InvalidCommandException If the command cannot be encoded.
   */
  static CompletableFuture<Boolean> submitConfigCommand(Command c, Runnable... andThen)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    final CompletableFuture<Boolean> submittedCmd = submitConfigCommand(c);
    submittedCmd.thenAccept(
        x -> { // only perform the runnable if the submittedCommand is accepted
          if (x) {
            for (Runnable then : andThen) {
              then.run();
            }
          }
        });
    return submittedCmd;
  }

  /**
   * Asynchronously submits an impedance command to this device using the
   * ImpedanceConfigurationTask.
   *
   * @param c Command to be sent to the device.
   * @return ImpedanceInfo containing slope and offset, or else null.
   */
  static CompletableFuture<ImpedanceInfoPacket> submitImpCommand(Command c)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    final byte[] encodedBytes = encodeCommand(c);
    return submitCommand(
        new ImpedanceConfigurationTask(BluetoothManager.getOutputStream(), encodedBytes), null);
  }

  static byte[] encodeCommand(Command c) throws InvalidCommandException {
    final byte[] encodedBytes = MentalabCodec.encodeCommand(c);
    if (encodedBytes == null) {
      throw new InvalidCommandException("Failed to encode command. Exiting.");
    }
    return encodedBytes;
  }
}
