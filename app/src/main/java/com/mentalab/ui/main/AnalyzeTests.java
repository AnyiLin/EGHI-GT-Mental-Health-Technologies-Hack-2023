package com.mentalab.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mentalab.R;
import com.mentalab.databinding.AnalyzeTestsBinding;
import com.mentalab.databinding.MainpageBinding;

import java.util.ArrayList;

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

        addScrollView();
    }

    public void addScrollView() {
        binding.analyzeTestsLinearLayout.removeAllViews();
        binding.analyzeTestsScrollView.setVisibility(View.VISIBLE);
        binding.analyzeTestsLinearLayout.setVisibility(View.VISIBLE);
        // TODO: add some method of finding the csv result files
        for (int counter = 0; counter < 2; counter++) { // TODO: when we actually have the csv files, make the loop actually iterate through them instead
            LinearLayout linearLayout = new LinearLayout(getContext());
            Button analyzeButton = new Button(getContext());
            TextView analyzeText = new TextView(getContext());
            linearLayout.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(analyzeButton);
            linearLayout.addView(analyzeText);
            analyzeButton.setText("Select");
            analyzeButton.setTextSize(toDp(10));
            analyzeButton.setAllCaps(false);
            analyzeButton.setTextColor(getResources().getColor(R.color.black));
            analyzeButton.setBackgroundTintList(getResources().getColorStateList(R.color.main_blue));
            analyzeButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            analyzeButton.setX(toDp(20));
            analyzeButton.setWidth((int) toDp(140));
            analyzeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: make selections and stuff
                    NavHostFragment.findNavController(AnalyzeTests.this)
                            .navigate(R.id.action_analyzeTests_to_results);
                }
            });
            analyzeText.setText("Test "+ (counter + 1)); // TODO: make this actually reflect the tests
            analyzeText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            analyzeText.setX(toDp(25));
            analyzeText.setTextSize(toDp(9));
            analyzeText.setY(toDp(0));
            binding.analyzeTestsLinearLayout.addView(linearLayout);
        }
    }

    public float toDp(int dp) {
        return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AnalyzeTestsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}