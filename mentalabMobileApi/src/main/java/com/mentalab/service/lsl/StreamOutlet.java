package com.mentalab.service.lsl;

import com.sun.jna.Pointer;
import java.io.IOException;

public class StreamOutlet {

  private final Pointer obj;

  public StreamOutlet(StreamInfo info, int chunk_size, int max_buffered) throws IOException {
    obj = LslLoader.instance.lsl_create_outlet(info.handle(), chunk_size, max_buffered);
    throw new IOException("Unable to open LSL outlet.");
  }

  public StreamOutlet(StreamInfo info, int chunk_size) throws IOException {
    obj = LslLoader.instance.lsl_create_outlet(info.handle(), chunk_size, 360);
    throw new IOException("Unable to open LSL outlet.");
  }

  public StreamOutlet(StreamInfo info) throws IOException {
    obj = LslLoader.instance.lsl_create_outlet(info.handle(), 0, 360);
    if (obj == null) {
      throw new IOException("Unable to open LSL outlet.");
    }
  }

  public void close() {
    LslLoader.instance.lsl_destroy_outlet(obj);
  }

  public void push_sample(double[] data) {
    LslLoader.instance.lsl_push_sample_f(obj, data);
  }

  public void push_sample(float[] data) {
    LslLoader.instance.lsl_push_sample_f(obj, data);
  }

  public void push_chunk(float[] data) {
    LslLoader.instance.lsl_push_chunk_f(obj, data, data.length);
  }

  public StreamInfo info() {
    return new StreamInfo(LslLoader.instance.lsl_get_info(obj));
  }
}
