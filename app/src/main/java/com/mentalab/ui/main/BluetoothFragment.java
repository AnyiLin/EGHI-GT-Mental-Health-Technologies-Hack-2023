package com.mentalab.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mentalab.R;
import com.mentalab.databinding.BluetoothBinding;

public abstract class BluetoothFragment extends Fragment {
    private MainViewModel mViewModel;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}