package com.frg96.careercompare.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.frg96.careercompare.R;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;


public class MainMenuFragment extends Fragment {

    private JobViewModel jobViewModel;

    Button mm_current_job_button;
    Button mm_job_offer_button;
    Button mm_comparison_weights_button;

    Button mm_compare_jobs_button;



    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        jobViewModel = new ViewModelProvider(requireActivity()).get(JobViewModel.class);

        mm_current_job_button = view.findViewById(R.id.mm_current_job_button);
        mm_job_offer_button = view.findViewById(R.id.mm_job_offer_button);
        mm_comparison_weights_button = view.findViewById(R.id.mm_comparison_weights_button);
        mm_compare_jobs_button = view.findViewById(R.id.mm_compare_jobs_button);

        mm_current_job_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_currentJobFragment);
        });

        mm_comparison_weights_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_comparisonWeightFragment);
        });

        mm_job_offer_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_jobOfferFragment);
        });

        mm_compare_jobs_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_listofJobFragment);
        });

        jobViewModel.canCompareFromMainMenu().observe(getViewLifecycleOwner(), enabled -> {
            mm_compare_jobs_button.setEnabled(Boolean.TRUE.equals(enabled));
        });

        return view;
    }
}