package com.mentalab.service.io;

import android.util.Log;
import com.mentalab.packets.Packet;
import com.mentalab.utils.Utils;
import com.mentalab.utils.constants.Topic;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class RecordSubscriber extends Subscriber<Void> {

  protected final BufferedWriter wr;
  protected double currentTimestamp;

  public RecordSubscriber(Topic t, BufferedWriter w) {
    super(t);
    this.wr = w;
  }

  protected static boolean requireNewLine(int channelCount, int i) {
    return i % channelCount == 0; // break line after 2, 4 or 8 entries
  }

  @Override
  public void accept(Packet packet) {
    try {
      currentTimestamp = packet.getTimeStamp();
      writePacketToCSV(packet.getData(), packet.getDataCount());
      wr.flush();
    } catch (IOException e) {
      Log.e(Utils.TAG, e.getMessage());
    }
  }

  protected void writePacketToCSV(List<Float> data, int dataCount) throws IOException {
    for (int i = 0; i < data.size(); i++) {
      if (requireNewLine(dataCount, i)) {
        initNewLine(currentTimestamp);
      }
      writeDataPoint(data.get(i));
    }
  }

  protected void writeDataPoint(Float v) throws IOException {
    wr.write(",");
    wr.write(v.toString());
  }

  protected void initNewLine(double ts) throws IOException {
    wr.newLine();
    wr.write(Utils.round(ts));
    wr.flush();
  }
}
