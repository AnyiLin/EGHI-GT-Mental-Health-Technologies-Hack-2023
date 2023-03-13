package com.mentalab;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.mentalab.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  public int timerMinutes;

  public boolean impMode = false;

  public boolean connected = false;

  private boolean bluetoothReturnBoolean;

  public ExploreDevice EEG;

  private void createBluetoothNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.bluetoothChannel);
      String description = getString(R.string.bluetoothChannelDescription);
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel channel = new NotificationChannel("Bluetooth Permissions Channel", name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
  private void createConnectionErrorNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = getString(R.string.bluetoothChannel);
      String description = getString(R.string.bluetoothChannelDescription);
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel channel = new NotificationChannel("Connection Error Channel", name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
  public void connectionErrorNotification() {
    createConnectionErrorNotificationChannel();
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Connection Error Channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: make this our app logo
            .setContentTitle("Failed to connect")
            .setContentText("Failed to connect to the device. Try again or ensure the device is connectable.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(1, builder.build());
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
              sendBluetoothNotification();
            }
          });

  public void sendBluetoothNotification() {
    createBluetoothNotificationChannel();
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Bluetooth Permissions Channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: make this our app logo
            .setContentTitle("Bluetooth Permissions Needed")
            .setContentText("Bluetooth (or Nearby Devices) permissions are required to run this app otherwise it will not function.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(0, builder.build());
  }

  public boolean checkBluetooth() {
    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
    {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
      {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
        return false;
      }
    }
    return true;
  }

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

  public void record(long timeMillis) {
    EEG.recordWithTimeout(getApplicationContext(), (int)timeMillis);
  }
}
