package com.mentalab.service.record;

import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import java.io.IOException;

public class RecordFile {

  private final ContentValues metaData = new ContentValues();

  public RecordFile(String filename) {
    this.metaData.put(MIME_TYPE, "text/csv");
    this.metaData.put(DISPLAY_NAME, filename);
  }

  public Uri createFile(Uri downloads, Context ctx) throws IOException {
    final Uri file = ctx.getContentResolver().insert(downloads, metaData); // thread safe
    if (file == null) {
      throw new IOException(
          "File already exists, please choose another filename. File path: " + downloads);
    }
    return file;
  }
}
