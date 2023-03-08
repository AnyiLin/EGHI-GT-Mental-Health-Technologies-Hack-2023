package com.mentalab.packets;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.utils.constants.Topic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Packet {

  private final double timeStamp;

  public Set<PacketDataType> type;
  public List<Float> data = new ArrayList<>();

  protected Packet(double timeStamp) {
    this.timeStamp = timeStamp;
  }

  /** Converts binary data stream to human-readable voltage values */
  public abstract void populate(byte[] data) throws InvalidDataException, IOException;

  /** String representation of attributes */
  @NonNull
  public abstract String toString();

  /** Number of elements in each packet */
  public int getDataCount() {
    return this.data.size();
  }

  /** Get data values from packet structure */
  public List<Float> getData() {
    return this.data;
  }

  /** Get timestamp of data retrieval from device*/
  public double getTimeStamp() {
    return this.timeStamp;
  }

  public abstract Topic getTopic();

  /** Clear data and reset */
  public Packet overwriteData(List<Float> newData) {
    this.data.clear();
    this.data = newData;
    return this;
  }
}
