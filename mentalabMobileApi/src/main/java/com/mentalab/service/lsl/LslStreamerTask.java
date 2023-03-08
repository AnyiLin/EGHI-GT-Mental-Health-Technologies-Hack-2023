package com.mentalab.service.lsl;

import android.util.Log;
import com.mentalab.ExploreDevice;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.Subscriber;
import com.mentalab.utils.constants.Topic;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class LslStreamerTask implements Callable<Boolean> {

  private static final int nominalSamplingRateOrientation = 20;
  private static final int dataCountOrientation = 9;
  static StreamOutlet lslStreamOutletExg;
  static StreamOutlet lslStreamOutletOrn;
  static StreamOutlet lslStreamOutletMarker;
  private static int samplingRate;
  private final ExploreDevice connectedDevice;

  private StreamInfo lslStreamInfoExg;

  public LslStreamerTask(ExploreDevice device) {
    this.connectedDevice = device;
    samplingRate = device.getSamplingRate().getAsInt();
  }

  @Override
  public Boolean call() throws InvalidDataException {
    try {

      lslStreamInfoExg =
          new StreamInfo(
              connectedDevice + "_ExG",
              "ExG",
              connectedDevice.getChannelCount().getAsInt(),
              samplingRate,
              ChannelFormat.FLOAT_32,
              connectedDevice + "_ExG");

      lslStreamOutletExg = new StreamOutlet(lslStreamInfoExg);

      StreamInfo lslStreamInfoOrn =
          new StreamInfo(
              connectedDevice.getDeviceName() + "_ORN",
              "ORN",
              dataCountOrientation,
              nominalSamplingRateOrientation,
              ChannelFormat.FLOAT_32,
              connectedDevice.getDeviceName() + "_ORN");
      lslStreamOutletOrn = new StreamOutlet(lslStreamInfoOrn);

      StreamInfo lslStreamInfoMarker =
          new StreamInfo(
              connectedDevice.getDeviceName() + "_Marker",
              "Markers",
              1,
              0,
              ChannelFormat.INT_32,
              connectedDevice.getDeviceName() + "_Markers");

      lslStreamOutletMarker = new StreamOutlet(lslStreamInfoMarker);

      ContentServer.getInstance()
          .registerSubscriber(
              new Subscriber<Packet>(Topic.EXG) {
                @Override
                public void accept(Packet packet) {
                  lslStreamOutletExg.push_chunk(convertArraylistToFloatArray(packet));
                }
              });

      ContentServer.getInstance()
          .registerSubscriber(
              new Subscriber<Packet>(Topic.ORN) {
                @Override
                public void accept(Packet packet) {
                  lslStreamOutletOrn.push_sample(convertArraylistToFloatArray(packet));
                }
              });

      ContentServer.getInstance()
          .registerSubscriber(
              new Subscriber<Packet>(Topic.MARKER) {
                @Override
                public void accept(Packet packet) {
                  lslStreamOutletMarker.push_sample(convertArraylistToFloatArray(packet));
                }
              });
    } catch (IOException exception) {
      throw new InvalidDataException("Error while stream LSL stream", null);
    }
    return null;
  }

  public void packetCallbackExG(Packet packet) {
    if (lslStreamInfoExg == null) {
      lslStreamInfoExg =
          new StreamInfo(
              connectedDevice + "_ExG",
              "ExG",
              packet.getDataCount(),
              250,
              ChannelFormat.FLOAT_32,
              connectedDevice + "_ExG");
      try {
        lslStreamOutletExg = new StreamOutlet(lslStreamInfoExg);
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    Log.d("TAG", "packetCallbackExG");
    lslStreamOutletExg.push_chunk(convertArraylistToFloatArray(packet));
  }

  float[] convertArraylistToFloatArray(Packet packet) {
    List<Float> packetVoltageValues = packet.getData();
    float[] floatArray = new float[packetVoltageValues.size()];
    packetVoltageValues.toArray();
    for (int index = 0; index < packetVoltageValues.size(); index++) {
      floatArray[index] = packetVoltageValues.get(index);
    }
    return floatArray;
  }
}
