package com.mentalab.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mentalab.MainActivity;
import com.mentalab.R;
import com.mentalab.databinding.MainpageBinding;

public class MainFragment extends Fragment {

    public MainViewModel mainViewModel;

    private MainpageBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = MainpageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity)getActivity()).checkBluetooth()) {
                    NavHostFragment.findNavController(MainFragment.this)
                            .navigate(R.id.action_mainFragment_to_bluetoothFragment);
                } else {
                    Toast toast = Toast.makeText(getContext(), "Bluetooth/Nearby Devices Permissions Needed", Toast.LENGTH_SHORT);
                    toast.show();
                    ((MainActivity)getActivity()).sendBluetoothNotification();
                }
            }
        });
        binding.instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_mainFragment_to_instructions);
            }
        });
        binding.testsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainActivity)getActivity()).checkBluetooth()) {
                    if (((MainActivity) getActivity()).connected ||true) { // TODO: remove the ||true for the final version
                        NavHostFragment.findNavController(MainFragment.this)
                                .navigate(R.id.action_mainFragment_to_runTest);
                    } else {
                        Toast toast = Toast.makeText(getContext(), "Please connect to a device", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(), "Bluetooth/Nearby Devices Permissions Needed", Toast.LENGTH_SHORT);
                    toast.show();
                    ((MainActivity)getActivity()).sendBluetoothNotification();
                }
            }
        });
        binding.analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_mainFragment_to_analyzeTests);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}