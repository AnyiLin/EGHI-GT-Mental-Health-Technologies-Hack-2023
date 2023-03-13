package com.mentalab.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mentalab.R;
import com.mentalab.databinding.InstructionsBinding;
import com.mentalab.databinding.LoadingPageBinding;

public class LoadingPage extends Fragment {

    LoadingPageBinding binding;

    // TODO: Rename and change types and number of parameters
    public static LoadingPage newInstance() {;
        return new LoadingPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = LoadingPageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}