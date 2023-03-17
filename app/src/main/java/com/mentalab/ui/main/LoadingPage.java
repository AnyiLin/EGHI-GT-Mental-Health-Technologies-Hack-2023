package com.mentalab.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mentalab.MainActivity;
import com.mentalab.R;
import com.mentalab.databinding.InstructionsBinding;
import com.mentalab.databinding.LoadingPageBinding;

public class LoadingPage extends Fragment {

    LoadingPageBinding binding;

    int timeMinutes;

    Thread main;

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

        binding.done.setVisibility(View.GONE);
        binding.done.setClickable(false);

        main = Thread.currentThread();

        timeMinutes = ((MainActivity)getActivity()).timerMinutes;
        long timeMillis = (long)timeMinutes*60*1000;
        long startTime = System.currentTimeMillis();
        //((MainActivity)getActivity()).record(timeMillis); //TODO: uncomment this for the final version
        new CountDownTimer(timeMillis, 1000){
            public void onTick(long millisUntilFinished){
                binding.timer.setText("Time Left:\n"+getTimer(startTime));
            }
            public  void onFinish(){
                binding.timer.setText("Time Left:\n"+getTimer(startTime));
                binding.done.setVisibility(View.VISIBLE);
                binding.done.setClickable(true);
            }
        }.start();

        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoadingPage.this)
                        .navigate(R.id.action_loadingPage_to_mainFragment);
            }
        });

    }

    public String getTimer(long startTime) {
        int timeLeft = 60*timeMinutes-(int)(getElapsedTime(startTime)/1000);
        int timerSeconds = timeLeft%60;
        int timerMinutes = timeLeft/60;
        if (timerSeconds<10) {
            return timerMinutes + ":0" + timerSeconds;
        }
        return timerMinutes + ":" + timerSeconds;
    }

    public long getElapsedTime(long startTime) {
        return System.currentTimeMillis()-startTime;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = LoadingPageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}