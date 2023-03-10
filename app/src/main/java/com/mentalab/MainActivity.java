package com.mentalab;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

  @RequiresApi(api = VERSION_CODES.R)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
