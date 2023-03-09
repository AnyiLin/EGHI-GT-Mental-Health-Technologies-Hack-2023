package com.example.eghihack.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;

import com.example.eghihack.R;
public class MainFragment extends Fragment {

    public MainViewModel mainViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button) getView().findViewById(
                R.id.button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_mainFragment_to_bluetooth);
            }
        });
    }

}