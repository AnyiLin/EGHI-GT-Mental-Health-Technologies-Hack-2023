package com.mentalab.service.lsl;

import android.util.Log;
import com.sun.jna.Native;

public class LslLoader {

  static lslLibLoader instance;

  static {
    Log.d("Lsl loader class", "Loading LSL library!!");

    instance = Native.load("lsl", lslLibLoader.class);
  }
}
