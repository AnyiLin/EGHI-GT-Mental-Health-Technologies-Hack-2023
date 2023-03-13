package com.mentalab.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mentalab.R;
import com.mentalab.databinding.AnalyzeTestsBinding;
import com.mentalab.databinding.MainpageBinding;

public class AnalyzeTests extends Fragment {

    AnalyzeTestsBinding binding;

    // TODO: Rename and change types and number of parameters
    public static AnalyzeTests newInstance() {;
        return new AnalyzeTests();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.analyzeTestsPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AnalyzeTests.this)
                        .navigate(R.id.action_analyzeTests_to_mainFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AnalyzeTestsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}