package com.mentalab.service.lsl;

import com.sun.jna.Pointer;

public class StreamInfo {

  private final Pointer obj;

  public StreamInfo(
      String name,
      String type,
      int channel_count,
      double nominal_srate,
      int channel_format,
      String source_id) {
    obj =
        LslLoader.instance.lsl_create_streaminfo(
            name, type, channel_count, nominal_srate, channel_format, source_id);
  }

  public StreamInfo(Pointer handle) {
    obj = handle;
  }

  /** Destroy a previously created StreamInfo object. */
  public void destroy() {
    LslLoader.instance.lsl_destroy_streaminfo(obj);
  }

  public Pointer handle() {
    return obj;
  }
}
