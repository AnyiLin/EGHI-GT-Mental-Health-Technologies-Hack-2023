package com.mentalab.service.io;

import com.mentalab.utils.constants.Topic;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class SampledRecordSubscriber extends RecordSubscriber {

  private final int sr;

  public SampledRecordSubscriber(Topic t, BufferedWriter w, int s) {
    super(t, w);
    this.sr = s;
  }

  @Override
  protected void writePacketToCSV(List<Float> data, int dataCount) throws IOException {
    for (int i = 0; i < data.size(); i++) {
      if (requireNewLine(dataCount, i)) {
        currentTimestamp += 1d / sr;
        initNewLine(currentTimestamp);
      }
      writeDataPoint(data.get(i));
    }
  }
}
