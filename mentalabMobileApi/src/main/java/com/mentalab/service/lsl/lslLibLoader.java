package com.mentalab.service.lsl;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface lslLibLoader extends Library {

  Pointer lsl_create_outlet(Pointer info, int chunk_size, int max_buffered);

  Pointer lsl_create_streaminfo(
      String name,
      String type,
      int channel_count,
      double nominal_state,
      int channel_format,
      String source_id);

  void lsl_destroy_streaminfo(Pointer info);

  Pointer lsl_get_info(Pointer obj);

  void lsl_destroy_outlet(Pointer obj);

  int lsl_push_sample_f(Pointer obj, double[] data);

  int lsl_push_sample_f(Pointer obj, float[] data);

  int lsl_push_chunk_f(Pointer obj, float[] data, int dataElements);
}
