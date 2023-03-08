package com.mentalab.service.record;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.mentalab.ExploreDevice;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.RecordSubscriber;
import com.mentalab.service.io.SampledRecordSubscriber;
import com.mentalab.utils.FileGenerator;
import com.mentalab.utils.Utils;
import com.mentalab.utils.constants.ChannelCount;
import com.mentalab.utils.constants.SamplingRate;
import com.mentalab.utils.constants.Topic;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;

public class RecordTask implements Callable<Boolean>, AutoCloseable {

  private static final int ORN_SR = 20;

  private final Context cxt;
  private final String filename;
  private final SamplingRate sr;
  private final ChannelCount count;

  private BufferedWriter eegWr;
  private BufferedWriter ornWr;
  private BufferedWriter markerWr;

  private RecordSubscriber exgSubscriber;
  private RecordSubscriber ornSubscriber;
  private RecordSubscriber markerSubscriber;

  public RecordTask(Context c, String filename, ExploreDevice e) {
    this.cxt = c;
    this.filename = filename;
    this.sr = e.getSamplingRate();
    this.count = e.getChannelCount();
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private static void writeHeader(BufferedWriter wr, String header) throws IOException {
    wr.write(header);
    wr.flush();
  }

  private static String buildEEGHeader(int channelCount) {
    final StringBuilder headerBuilder = new StringBuilder("TimeStamp,ch1,ch2,ch3,ch4");
    for (int i = 5; i <= channelCount; i++) {
      headerBuilder.append(",").append("ch").append(i);
    }
    return headerBuilder.toString();
  }

  private static BufferedWriter createNewBufferedWriter(Context c, Uri uri)
      throws FileNotFoundException {
    return new BufferedWriter(
        new OutputStreamWriter(c.getContentResolver().openOutputStream(uri, "wa")));
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  public Boolean call() throws IOException {
    final FileGenerator androidFileGenerator = new FileGenerator(cxt);

    final Uri exgFile = androidFileGenerator.generateFile(filename + "_Exg");
    final Uri ornFile = androidFileGenerator.generateFile(filename + "_Orn");
    final Uri markerFile = androidFileGenerator.generateFile(filename + "_Marker");

    recordEeg(exgFile);
    recordOrn(ornFile);
    recordMarker(markerFile);
    return true;
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void recordEeg(Uri exgFile) throws IOException {
    this.eegWr = createNewBufferedWriter(cxt, exgFile);
    this.exgSubscriber = new SampledRecordSubscriber(Topic.EXG, eegWr, sr.getAsInt());

    writeHeader(eegWr, buildEEGHeader(count.getAsInt()));
    ContentServer.getInstance().registerSubscriber(exgSubscriber);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void recordOrn(Uri ornFile) throws IOException {
    this.ornWr = createNewBufferedWriter(cxt, ornFile);
    this.ornSubscriber = new SampledRecordSubscriber(Topic.ORN, ornWr, ORN_SR);

    writeHeader(ornWr, "TimeStamp,ax,ay,az,gx,gy,gz,mx,my,mz");
    ContentServer.getInstance().registerSubscriber(ornSubscriber);
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void recordMarker(Uri markerFile) throws IOException {
    this.markerWr = createNewBufferedWriter(cxt, markerFile);
    this.markerSubscriber = new RecordSubscriber(Topic.MARKER, markerWr);

    writeHeader(markerWr, "TimeStamp,Code");
    ContentServer.getInstance().registerSubscriber(markerSubscriber);
  }

  @Override
  public void close() {
    ContentServer.getInstance().deRegisterSubscriber(exgSubscriber);
    ContentServer.getInstance().deRegisterSubscriber(ornSubscriber);
    ContentServer.getInstance().deRegisterSubscriber(markerSubscriber);
    try {
      eegWr.close();
      ornWr.close();
      markerWr.close();
    } catch (IOException e) {
      // unlikely this will occur if we successfully wrote to file
      Log.e(Utils.TAG, "Failed to close writer. Ignored.", e);
    }
  }
}
