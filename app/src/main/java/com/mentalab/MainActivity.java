package com.mentalab;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.mentalab.exception.CommandFailedException;
import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.sensors.MarkerPacket;
import com.mentalab.packets.sensors.exg.EEGPacket;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.Subscriber;
import com.mentalab.utils.commandtranslators.Command;
import com.mentalab.utils.constants.Topic;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

  @RequiresApi(api = VERSION_CODES.R)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    int device = 1;
    boolean impMode = false;

    try {
      ExploreDevice connect = null;
      if(device == 0) connect = MentalabCommands.connect("8524");
      else if(device == 1) connect = MentalabCommands.connect("CA14");
      else if(device == 2) connect = MentalabCommands.connect("844C");
      else throw new IllegalArgumentException("Device ID unknown");
      connect.acquire();
      Subscriber<EEGPacket> sub = null;
      Subscriber<MarkerPacket> markerSub = new Subscriber<MarkerPacket>(Topic.MARKER) {
        @Override
        public void accept(Packet packet) {
          Log.d("Got marker", packet.getData().toString());
        }
      };

      if(impMode) {
        connect.calculateImpedance(); // something here is wrong for 32 channels
        sub = new Subscriber<EEGPacket>(Topic.IMPEDANCE) {
          @Override
          public void accept(Packet packet) {
            Log.d("DEBUG__ZZ", packet.getData().toString());
          }
        };
      }
      else {
        sub = new Subscriber<EEGPacket>(Topic.EXG) {
          @Override
          public void accept(Packet packet) {
            Log.d("DEBUG__ZZ", packet.getData().toString());
          }
        };
      }
      ContentServer.getInstance().registerSubscriber(sub);
      ContentServer.getInstance().registerSubscriber(markerSub);

      // To get last connected device after sending any command/connection drop: use
      // getLastConnectedDevice() method of MentalabCodec
    }
    // catch (NoBluetoothException | NoConnectionException | IOException | ExecutionException |
    // InterruptedException e) {
    catch (NoBluetoothException
        | NoConnectionException
        | IOException
        | ExecutionException
        | InterruptedException
        | InvalidCommandException
        | CommandFailedException e) {
      e.printStackTrace();
    }
  }

  // Can be used to test if UI Thread is blocked while running demo
  public void sayHello(View view) {
    Toast toast = Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT);
    toast.show();
  }
}
