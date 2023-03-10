package com.mentalab.ui.main;

import androidx.annotation.IdRes;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mentalab.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    public MainViewModel mainViewModel;

    public ArrayList<Integer> bluetoothScrollIDs;

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
                        .navigate(R.id.action_mainFragment_to_bluetoothFragment);
            }
        });

        bluetoothScrollIDs = addBluetoothConnectScrollView();
    }

    public ArrayList<Integer> addBluetoothConnectScrollView() {
        ScrollView bluetoothScroll = (ScrollView) getView().findViewById(R.id.bluetoothScrollView);
        bluetoothScroll.setVisibility(View.VISIBLE);
        ArrayList<Integer> linearLayoutIDList = new ArrayList<>();
        for (int counter = 0; counter < 3; counter++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            Button connectButton = new Button(getContext());
            TextView connectText = new TextView(getContext());
            linearLayout.addView(connectButton);
            linearLayout.addView(connectText);
            connectButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            connectButton.setText("Connect");
            connectText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            connectText.setText("device "+counter);
            bluetoothScroll.addView(linearLayout);
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            int newId = View.generateViewId();
            linearLayout.setId(newId);
            linearLayoutIDList.add(newId);
        }
        return linearLayoutIDList;
    }

}