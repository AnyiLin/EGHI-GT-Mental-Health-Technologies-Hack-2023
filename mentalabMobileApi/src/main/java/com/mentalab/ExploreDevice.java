package com.mentalab;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.mentalab.exception.CommandFailedException;
import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.packets.info.ImpedanceInfoPacket;
import com.mentalab.service.decode.MentalabCodec;
import com.mentalab.service.impedance.ImpedanceCalculatorTask;
import com.mentalab.service.lsl.LslStreamerTask;
import com.mentalab.service.record.RecordTask;
import com.mentalab.utils.ConfigSwitch;
import com.mentalab.utils.Utils;
import com.mentalab.utils.commandtranslators.Command;
import com.mentalab.utils.constants.ChannelCount;
import com.mentalab.utils.constants.ConfigProtocol;
import com.mentalab.utils.constants.SamplingRate;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/** A wrapper around BluetoothDevice */
public class ExploreDevice {

  private final BluetoothDevice btDevice;
  private final String deviceName;
  private ImpedanceCalculatorTask calculateImpedanceTask = new ImpedanceCalculatorTask(this);
  private ChannelCount channelCount = ChannelCount.CC_8;
  private SamplingRate samplingRate = SamplingRate.SR_250;
  private int channelMask = 0b11111111; // Initialization assumes the device has 8 channels
  private volatile float slope = 223660;
  private volatile double offset = 42.218;
  private RecordTask recordTask;

  public ExploreDevice(BluetoothDevice btDevice, String deviceName) {
    this.btDevice = btDevice;
    this.deviceName = deviceName;
  }

  public ExploreDevice(ExploreDevice device) {
    this.btDevice = device.btDevice;
    this.deviceName = device.deviceName;
    this.calculateImpedanceTask = device.calculateImpedanceTask;
    this.channelCount = device.channelCount;
    this.channelMask = device.channelMask; // Initialization assumes the device has 8 channels
    this.slope = 223660;
    this.offset = device.offset;
    this.samplingRate = device.samplingRate;
    this.recordTask = device.recordTask;

  }

  private static void waitOnConfig(List<CompletableFuture<Boolean>> deviceConfig)
      throws ExecutionException, InterruptedException, IOException {
    Log.i(Utils.TAG, "Waiting on initial configuration.");
    for (CompletableFuture<Boolean> f : deviceConfig) {
      if (!f.get()) {
        MentalabCommands.shutdown();
        throw new IOException("Unable to initialise device. Cannot proceed.");
      }
    }
  }

  private static int bitShift(int binaryArg, ConfigSwitch s) {
    final int channelID = s.getProtocol().getID();
    final int on = s.isOn() ? 1 : 0; // on = 1, off = 0
    if ((binaryArg >> channelID & on) != 1) { // if binaryArg at channel id is on or off
      binaryArg ^= (1 << channelID); // flip bit if necessary at the channel id
    }
    return binaryArg;
  }

  private static Command generateModuleCommand(ConfigSwitch module) {
    final Command c = module.isOn() ? Command.CMD_MODULE_ENABLE : Command.CMD_MODULE_DISABLE;
    c.setArg(module.getProtocol().getID());
    return c;
  }

  /** Returns the device data stream. */
  public static InputStream getInputStream() throws NoBluetoothException, IOException {
    return BluetoothManager.getInputStream();
  }

  BluetoothDevice getBluetoothDevice() {
    return btDevice;
  }

  /**
   * Start acquiring data from this device.
   *
   * <p>Before reading the Bluetooth input stream, two tasks are assigned that wait for packets and
   * configure the device based on those packets. If these packets are not received, or the device
   * is not configured, we cannot proceed.
   */
  public ExploreDevice acquire()
      throws IOException, NoBluetoothException, ExecutionException, InterruptedException {
    List<CompletableFuture<Boolean>> deviceConfig =
        Arrays.asList(
            CompletableFuture.supplyAsync(new ConfigureChannelCountTask(this)),
            CompletableFuture.supplyAsync(new ConfigureDeviceInfoTask(this))); // start config first
    MentalabCodec.getInstance().decodeInputStream(getInputStream(), this);
    waitOnConfig(deviceConfig); // wait on config, otherwise connection failed
    return this;
  }

  /**
   * Enables or disables data collection of a channel. Channels will be set in bulk out of order.
   *
   * <p>By default data from all channels is collected. Disable channels you do not need to save
   * bandwidth and power.
   *
   * @param switches List of channels to set on (true) or off (false) channel0 ... channel7
   * @throws InvalidCommandException If the provided Switches are not all type Channel.
   */
  public Future<Boolean> setChannels(Set<ConfigSwitch> switches)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    Utils.checkSwitchTypes(switches, ConfigProtocol.Type.Channel);
    switches = Utils.removeRedundantSwitches(switches, this.getChannelCount());
    final Command c = generateChannelCommand(switches);
    return DeviceManager.submitConfigCommand(c, () -> setChannelMask(c.getArg()));
  }

  private Command generateChannelCommand(Set<ConfigSwitch> channelSwitches) {
    final Command c = Command.CMD_CHANNEL_SET;
    c.setArg(generateChannelCmdArg(channelSwitches));
    return c;
  }

  private int generateChannelCmdArg(Set<ConfigSwitch> switches) {
    for (ConfigSwitch s : switches) {
      channelMask = bitShift(channelMask, s);
    }
    return channelMask;
  }

  /** Set a single channel on or off. */
  public Future<Boolean> setChannel(ConfigSwitch channel)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    final Set<ConfigSwitch> channelToList = new HashSet<>();
    channelToList.add(channel);
    return setChannels(channelToList);
  }

  /**
   * Enables or disables data collection of a module.
   *
   * <p>By default data from all modules is collected. Disable modules you do not need to save
   * bandwidth and power.
   */
  public Future<Boolean> setModule(ConfigSwitch mSwitch)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    Utils.checkSwitchType(mSwitch, ConfigProtocol.Type.Module);
    final Command c = generateModuleCommand(mSwitch);
    return DeviceManager.submitConfigCommand(c);
  }

  /**
   * Sets sampling rate of the device
   *
   * <p>Sampling rate only applies to ExG data. Orientation and Environment data are always sampled
   * at 20Hz.
   */
  public CompletableFuture<Boolean> setSamplingRate(SamplingRate sr)
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    final Command c = Command.CMD_SAMPLING_RATE_SET;
    c.setArg(sr.getCode());
    return DeviceManager.submitConfigCommand(c, () -> setSR(sr));
  }

  /** Formats internal memory of device. */
  public Future<Boolean> formatMemory()
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    return DeviceManager.submitConfigCommand(Command.CMD_MEMORY_FORMAT);
  }

  /**
   * Formats internal memory of device. However, when the sampling rate has changed, this command
   * fails.
   */
  public Future<Boolean> softReset()
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    return DeviceManager.submitConfigCommand(Command.CMD_SOFT_RESET);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public Future<Boolean> record(Context cxt, String filename) throws RejectedExecutionException {
    recordTask = new RecordTask(cxt, filename, this);
    return DeviceManager.submitTask(recordTask);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public Future<Boolean> record(Context cxt) throws RejectedExecutionException {
    final String filename = String.valueOf(System.currentTimeMillis());
    return record(cxt, filename);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public Future<Boolean> recordWithTimeout(Context cxt, int millis)
      throws RejectedExecutionException {
    final String filename = String.valueOf(System.currentTimeMillis());
    recordTask = new RecordTask(cxt, filename, this);
    return DeviceManager.submitTimeoutTask(recordTask, millis, () -> recordTask.close());
  }

  public boolean stopRecord() {
    if (recordTask == null) {
      return false;
    }
    recordTask.close();
    return true;
  }

  public Future<Boolean> pushToLSL() throws RejectedExecutionException {
    return DeviceManager.submitTask(new LslStreamerTask(this));
  }

  public Future<Boolean> calculateImpedance()
      throws RejectedExecutionException, IOException, ExecutionException, InterruptedException,
          InvalidCommandException, NoBluetoothException, CommandFailedException {
    startImpedanceMode();
    ExploreExecutor.getInstance().resetExecutorServices();
    return DeviceManager.submitTask(calculateImpedanceTask);
  }

  private void startImpedanceMode()
      throws RejectedExecutionException, IOException, ExecutionException, InterruptedException,
          InvalidCommandException, NoBluetoothException, CommandFailedException {
    final ImpedanceInfoPacket slopeOffset =
        DeviceManager.submitImpCommand(Command.CMD_ZM_ENABLE).get();
    if (slopeOffset != null) {
      this.setSlope(slopeOffset.getSlope());
      this.setOffset(slopeOffset.getOffset());
    }
  }

  public Future<Boolean> stopImpedanceCalculation()
      throws RejectedExecutionException, IOException, InvalidCommandException,
          NoBluetoothException {
    final Command c = Command.CMD_ZM_DISABLE;
    calculateImpedanceTask.cancelTask();
    return DeviceManager.submitConfigCommand(
        c);
  }

  public String getDeviceName() {
    return this.deviceName;
  }

  public ChannelCount getChannelCount() {
    return this.channelCount;
  }

  public void setChannelCount(ChannelCount count) {
    Log.d(Utils.TAG, "Channel count set to: " + count);
    this.channelCount = count;
  }

  public SamplingRate getSamplingRate() {
    return this.samplingRate;
  }

  public float getSlope() {
    return this.slope;
  }

  private void setSlope(float slope) {
    Log.d(Utils.TAG, "Impedance slope set to: " + slope);
    this.slope = slope;
  }

  public double getOffset() {
    return this.offset;
  }

  private void setOffset(double offset) {
    Log.d(Utils.TAG, "Impedance offset set to: " + offset);
    this.offset = offset;
  }

  public int getChannelMask() {
    return this.channelMask;
  }

  public void setChannelMask(int mask) {
    Log.d(
        Utils.TAG, "Channel mask set to: " + Utils.intToBinaryString(mask, this.getChannelCount()));
    this.channelMask = mask;
  }

  public void setSR(SamplingRate sr) {
    Log.d(Utils.TAG, "Sampling rate set to: " + sr);
    this.samplingRate = sr;
  }

  /* Gets bluetooth packet sample size depending on number of channels*/
  public int getSampleSize() {
    return this.channelCount.getSampleSize();
  }
}
