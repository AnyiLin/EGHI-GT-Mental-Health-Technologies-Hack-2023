package com.mentalab.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import com.mentalab.service.record.RecordFile;
import java.io.IOException;

public class FileGenerator {

  private static final String RESERVED_CHARS = "|\\?*<\":>+[]/'";

  private final Uri directory;
  private final Context context;

  public FileGenerator(Context context) {
    this.context = context;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      this.directory = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
    } else {
      this.directory =
          Uri.fromFile(
              Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
    }
  }

  private static void validateFilename(String filename) throws IOException {
    if (filename.length() < 1) {
      throw new IOException("Filename is empty.");
    }
    checkValidChars(filename);
  }

  private static void checkValidChars(String filename) throws IOException {
    for (int i = 0; i < filename.length(); i++) {
      char c = filename.charAt(i);
      checkChar(c);
    }
  }

  private static void checkChar(char c) throws IOException {
    if (RESERVED_CHARS.indexOf(c) > -1) {
      throw new IOException("Invalid filename, contains character: " + c);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public Uri generateFile(String filename) throws IOException {
    validateFilename(filename);

    final RecordFile file = new RecordFile(filename);
    return file.createFile(directory, context);
  }
}
