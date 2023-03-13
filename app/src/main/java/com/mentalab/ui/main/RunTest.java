package com.mentalab.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mentalab.MainActivity;
import com.mentalab.R;
import com.mentalab.databinding.RunTestBinding;

public class RunTest extends Fragment {

    RunTestBinding binding;

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
                        .navigate(R.id.action_runTest_to_mainFragment);
            }
        });
        binding.runTestButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (binding.buttonGroup.getCheckedRadioButtonId() < 0) {
                    ((MainActivity)getActivity()).timerMinutes = 10;
                } else if (binding.buttonGroup.getCheckedRadioButtonId() == binding.tenMinuteButton.getId()) {
                    ((MainActivity)getActivity()).timerMinutes = 10;
                } else if (binding.buttonGroup.getCheckedRadioButtonId() == binding.fifteenMinuteButton.getId()) {
                    ((MainActivity)getActivity()).timerMinutes = 15;
                } else if (binding.buttonGroup.getCheckedRadioButtonId() == binding.twentyMinuteButton.getId()) {
                    ((MainActivity)getActivity()).timerMinutes = 20;
                } else if (binding.buttonGroup.getCheckedRadioButtonId() == binding.otherButton.getId()) {
                    if (binding.otherInput.getText().toString().equals("")) {
                        ((MainActivity)getActivity()).timerMinutes = 10;
                    } else {
                        try {
                            ((MainActivity)getActivity()).timerMinutes = Integer.parseInt(binding.otherInput.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast toast = Toast.makeText(getContext(), "You wanna sit here forever or something?", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                }
                Log.d("timer", ((MainActivity)getActivity()).timerMinutes+"");
                NavHostFragment.findNavController(RunTest.this)
                        .navigate(R.id.action_runTest_to_loadingPage);
            }
        });
        binding.otherInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.otherButton.setChecked(true);
                return false;
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