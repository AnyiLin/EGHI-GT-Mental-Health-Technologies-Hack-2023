package com.mentalab;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mentalab.databinding.ActivityMainBinding;
import com.mentalab.exception.CommandFailedException;
import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.sensors.MarkerPacket;
import com.mentalab.packets.sensors.exg.EEGPacket;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.Subscriber;
import com.mentalab.ui.main.MainFragment;
import com.mentalab.utils.commandtranslators.Command;
import com.mentalab.utils.constants.Topic;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;

  public boolean impMode = false;
  public int device = 1;

  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.bluetoothChannel);
      String description = getString(R.string.bluetoothChannelDescription);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("Bluetooth Permissions Channel", name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
  private ActivityResultLauncher<String> requestPermissionLauncher =
          registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
              // Permission is granted. Continue the action or workflow in your
              // app.
            } else {
              // Explain to the user that the feature is unavailable because the
              // feature requires a permission that the user has denied. At the
              // same time, respect the user's decision. Don't link to system
              // settings in an effort to convince the user to change their
              // decision.
              createNotificationChannel();
              NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Bluetooth Permissions Channel")
                      .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: make this our app logo
                      .setContentTitle("Bluetooth Permissions Needed")
                      .setContentText("Bluetooth permissions are required to run this app otherwise it will not function.")
                      .setPriority(NotificationCompat.PRIORITY_DEFAULT);
              NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

              // notificationId is a unique int for each notification that you must define
              notificationManager.notify(0, builder.build());
            }
          });

  @RequiresApi(api = VERSION_CODES.R)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestPermissionLauncher.launch(
            Manifest.permission.BLUETOOTH_CONNECT);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    NavHostFragment navHostFragment =
            (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
    NavController navController = navHostFragment.getNavController();
  }

  // Can be used to test if UI Thread is blocked while running demo
  public void sayHello(View view) {
    Toast toast = Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT);
    toast.show();
  }

  public boolean getImpMode() {
    return impMode;
  }
}
