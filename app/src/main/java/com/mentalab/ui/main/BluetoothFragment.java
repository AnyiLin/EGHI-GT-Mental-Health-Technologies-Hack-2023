package com.mentalab.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Visibility;

import com.mentalab.ExploreDevice;
import com.mentalab.MainActivity;
import com.mentalab.MentalabCommands;
import com.mentalab.R;
import com.mentalab.databinding.BluetoothBinding;
import com.mentalab.exception.CommandFailedException;
import com.mentalab.exception.InvalidCommandException;
import com.mentalab.exception.NoBluetoothException;
import com.mentalab.exception.NoConnectionException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.sensors.MarkerPacket;
import com.mentalab.packets.sensors.exg.EEGPacket;
import com.mentalab.service.io.ContentServer;
import com.mentalab.service.io.Subscriber;
import com.mentalab.utils.constants.Topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BluetoothFragment extends Fragment {
    public MainViewModel mainViewModel;

    public ArrayList<Integer> bluetoothScrollIDs;

    private BluetoothBinding binding;

    public static BluetoothFragment newInstance() {
        return new BluetoothFragment() {
        };
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = BluetoothBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.bluetoothPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(BluetoothFragment.this)
                        .navigate(R.id.action_bluetoothFragment_to_mainFragment);
            }
        });

        binding.bluetoothPageRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToEEG();
                bluetoothScrollIDs = addBluetoothConnectScrollView();
            }
        });

        connectToEEG();

        bluetoothScrollIDs = addBluetoothConnectScrollView();
    }

    public void connectToEEG() {
        boolean impMode = ((MainActivity)getActivity()).impMode;
        int device = ((MainActivity)getActivity()).device;
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
        };
    }

    public ArrayList<Integer> addBluetoothConnectScrollView() {
        binding.linearLayout.removeAllViews();
        binding.bluetoothScrollView.setVisibility(View.VISIBLE);
        binding.linearLayout.setVisibility(View.VISIBLE);
        ArrayList<Integer> linearLayoutIDList = new ArrayList<>();
        for (int counter = 0; counter < 3; counter++) { // TODO: Make this actually reflect all scanned bluetooth devices
            LinearLayout linearLayout = new LinearLayout(getContext());
            Button connectButton = new Button(getContext());
            TextView connectText = new TextView(getContext());
            linearLayout.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(connectButton);
            linearLayout.addView(connectText);
            connectButton.setText("Connect");
            connectButton.setTextSize(toDp(7));
            connectButton.setAllCaps(false);
            connectButton.setTextColor(getResources().getColor(R.color.black));
            connectButton.setBackgroundTintList(getResources().getColorStateList(R.color.main_blue));
            connectButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            connectButton.setX(toDp(20));
            connectButton.setWidth((int)toDp(100));
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Connect to the device found
                }
            });
            connectText.setText("device "+counter); // TODO: Make this actually display the device name
            connectText.setX(toDp(25));
            connectText.setTextSize(toDp(7));
            connectText.setY(toDp(2));
            binding.linearLayout.addView(linearLayout);
            int newId = View.generateViewId();
            linearLayout.setId(newId);
            linearLayoutIDList.add(newId);
        }
        return linearLayoutIDList;
    }

    public float toDp(int dp) {
        return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}