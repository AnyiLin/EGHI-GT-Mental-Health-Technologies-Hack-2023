package com.mentalab.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mentalab.R;
import com.mentalab.databinding.LoadingPageBinding;
import com.mentalab.databinding.RunTestBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RunTest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunTest extends Fragment {

    RunTestBinding binding;

    // TODO: Rename and change types and number of parameters
    public static RunTest newInstance() {;
        return new RunTest();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.runTestPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RunTest.this)
                        .navigate(R.id.action_instructions_to_mainFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = RunTestBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}