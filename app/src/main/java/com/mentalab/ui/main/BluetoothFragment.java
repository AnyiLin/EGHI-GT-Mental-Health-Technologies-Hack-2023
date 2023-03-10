package com.mentalab.ui.main;

import android.os.Bundle;
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

import com.mentalab.R;
import com.mentalab.databinding.BluetoothBinding;

import java.util.ArrayList;

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

        bluetoothScrollIDs = addBluetoothConnectScrollView();
    }

    public ArrayList<Integer> addBluetoothConnectScrollView() {
        binding.linearLayout.removeAllViews();
        binding.bluetoothScrollView.setVisibility(View.VISIBLE);
        binding.linearLayout.setVisibility(View.VISIBLE);
        ArrayList<Integer> linearLayoutIDList = new ArrayList<>();
        for (int counter = 0; counter < 3; counter++) {
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
            connectText.setText("device "+counter);
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