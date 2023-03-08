package com.mentalab;

import static com.mentalab.utils.Utils.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {

  private static final String UUID_BLUETOOTH_SPP = "00001101-0000-1000-8000-00805f9b34fb";
  private static BluetoothSocket mmSocket = null;

  private static Set<BluetoothDevice> getBondedDevices() throws NoBluetoothException {
    Log.i(TAG, "Searching for nearby devices...");
    final Set<BluetoothDevice> bondedDevices = getBluetoothAdapter().getBondedDevices();
    if (bondedDevices == null) {
      throw new NoBluetoothException("No Bluetooth devices available.");
    }
    return bondedDevices;
  }

  protected static Set<BluetoothDevice> getBondedExploreDevices() throws NoBluetoothException {
    final Set<BluetoothDevice> bondedDevices = BluetoothManager.getBondedDevices();
    return getAllExploreDevices(bondedDevices);
  }

  private static Set<BluetoothDevice> getAllExploreDevices(Set<BluetoothDevice> bondedDevices) {
    final Set<BluetoothDevice> bondedExploreDevices = new HashSet<>();
    for (BluetoothDevice bt : bondedDevices) {
      final String name = bt.getName();
      addDeviceIfCorrectName(bondedExploreDevices, bt, name);
    }
    return bondedExploreDevices;
  }

  private static void addDeviceIfCorrectName(
      Set<BluetoothDevice> bondedExploreDevices, BluetoothDevice bt, String name) {
    if (name.startsWith("Explore_")) {
      bondedExploreDevices.add(bt);
      Log.i(Utils.TAG, "Explore device available: " + name);
    }
  }

  private static BluetoothAdapter getBluetoothAdapter() throws NoBluetoothException {
    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    if (btAdapter == null) {
      throw new NoBluetoothException("Bluetooth service not available. Exiting.");
    } else if (!btAdapter.isEnabled()) {
      throw new NoBluetoothException("Bluetooth is not enabled. Exiting.");
    }
    return btAdapter;
  }

  private static void establishRFCommWithDevice(BluetoothDevice device)
      throws NoConnectionException, IOException {
    closeSocket();
    try {
      mmSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_BLUETOOTH_SPP));
    } catch (Exception e) {
      closeSocket();
      throw new NoConnectionException("Connection to device failed.", e);
    }
    Log.i(TAG, "Received rfComm socket.");
  }

  protected static ExploreDevice connectToDevice(ExploreDevice device)
      throws NoConnectionException, IOException {
    establishRFCommWithDevice(device.getBluetoothDevice());
    try {
      mmSocket.connect();
    } catch (IOException e) {
      BluetoothManager.closeSocket();
      throw new IOException(e);
    }
    return device;
  }

  protected static void closeSocket() throws IOException {
    if (mmSocket == null) {
      return;
    }
    mmSocket.close();
    mmSocket = null;
  }

  protected static InputStream getInputStream() throws NoBluetoothException, IOException {
    if (BluetoothManager.mmSocket == null) {
      throw new NoBluetoothException("No Bluetooth socket available.");
    }
    return mmSocket.getInputStream();
  }

  protected static OutputStream getOutputStream() throws NoBluetoothException, IOException {
    if (mmSocket == null) {
      throw new NoBluetoothException("No Bluetooth socket available.");
    }
    return mmSocket.getOutputStream();
  }

  public static BluetoothSocket getBtSocket(){
    return mmSocket;
  }
}
