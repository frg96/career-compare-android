package com.frg96.careercompare.ui.weights;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.frg96.careercompare.R;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;
import com.google.android.material.slider.Slider;

public class ComparisonWeightFragment extends Fragment {
    private JobViewModel jobViewModel;

    Button cw_cancel_button;
    Button cw_save_button;

    Slider cw_salary_weight_slide;
    Slider cw_bonus_weight_slide;
    Slider cw_match401k_weight_slide;
    Slider cw_internet_stipend_weight_slide;
    Slider cw_accident_insurance_weight_slide;
    Slider cw_tuition_reimbursement_weight_slide;

    public ComparisonWeightFragment() {
        //empty constructor
    }

    @Override //restores the UI state if the activity is recreated.
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment- converts xml to view
        View view = inflater.inflate(R.layout.fragment_comparison_weight, container, false);

        // viewmodel is tied to the activity, gets the instance of jobviewmodel, can update UI when data changes
        jobViewModel = new ViewModelProvider(requireActivity()).get(JobViewModel.class);

        cw_salary_weight_slide = view.findViewById(R.id.cw_salary_weight_slider);
        cw_bonus_weight_slide = view.findViewById(R.id.cw_bonus_weight_slider);
        cw_match401k_weight_slide = view.findViewById(R.id.cw_match401k_weight_slider);
        cw_internet_stipend_weight_slide = view.findViewById(R.id.cw_internet_stipend_weight_slider);
        cw_accident_insurance_weight_slide = view.findViewById(R.id.cw_accident_insurance_weight_slider);
        cw_tuition_reimbursement_weight_slide = view.findViewById(R.id.cw_tuition_reimbursement_weight_slider);

        setSliderHapticFeedback(cw_salary_weight_slide);
        setSliderHapticFeedback(cw_bonus_weight_slide);
        setSliderHapticFeedback(cw_match401k_weight_slide);
        setSliderHapticFeedback(cw_internet_stipend_weight_slide);
        setSliderHapticFeedback(cw_accident_insurance_weight_slide);
        setSliderHapticFeedback(cw_tuition_reimbursement_weight_slide);

        cw_cancel_button = view.findViewById(R.id.cw_cancel_button);
        cw_save_button = view.findViewById(R.id.cw_save_button);

        //clear text fields or set to current weights
        jobViewModel.getCurrentWeights().observe(getViewLifecycleOwner(), weights -> {
            if(weights == null) {
                cw_save_button.setText("Save Weights");
            }
            else {
                cw_salary_weight_slide.setValue(weights.yearlySalaryWeight);
                cw_bonus_weight_slide.setValue(weights.yearlyBonusWeight);
                cw_match401k_weight_slide.setValue(weights.match401kWeight);
                cw_internet_stipend_weight_slide.setValue(weights.internetStipendWeight);
                cw_accident_insurance_weight_slide.setValue(weights.accidentInsuranceWeight);
                cw_tuition_reimbursement_weight_slide.setValue(weights.tuitionReimbursementWeight);

                cw_save_button.setText("Update Weights");
            }
        });

        // cancel button functionality
        cw_cancel_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false); // return to main menu
        });


        //save button functionality
        cw_save_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Weights weights = new Weights();
            weights.yearlySalaryWeight = Math.round(cw_salary_weight_slide.getValue());
            weights.yearlyBonusWeight = Math.round(cw_bonus_weight_slide.getValue());
            weights.match401kWeight = Math.round(cw_match401k_weight_slide.getValue());
            weights.internetStipendWeight = Math.round(cw_internet_stipend_weight_slide.getValue());
            weights.accidentInsuranceWeight = Math.round(cw_accident_insurance_weight_slide.getValue());
            weights.tuitionReimbursementWeight = Math.round(cw_tuition_reimbursement_weight_slide.getValue());

            jobViewModel.saveCurrentWeights(weights);
            Toast.makeText(requireContext(), "Weights saved", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false); // return to main menu

        });

        return view;
    }

    private void setSliderHapticFeedback(Slider slider) {
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            if (fromUser) {
                slider1.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                slider.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                slider.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });
    }
}
