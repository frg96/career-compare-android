package com.frg96.careercompare.ui.currentjob;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.frg96.careercompare.R;
import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;

public class CurrentJobFragment extends Fragment {

    private JobViewModel jobViewModel;

    EditText cj_title;
    EditText cj_company;
    EditText cj_city;
    EditText cj_state;
    EditText cj_col_index;
    EditText cj_yearly_salary;
    EditText cj_yearly_bonus;
    EditText cj_401k_match;
    EditText cj_internet_stipend;
    EditText cj_accident_insurance;
    EditText cj_tuition_reimbursement;

    Button cj_cancel_button;
    Button cj_save_button;

    public CurrentJobFragment() {
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
        View view = inflater.inflate(R.layout.fragment_current_job, container, false);

        jobViewModel = new ViewModelProvider(requireActivity()).get(JobViewModel.class);

        cj_title = view.findViewById(R.id.cj_title_edittext);
        cj_company = view.findViewById(R.id.cj_company_edittext);
        cj_city = view.findViewById(R.id.cj_city_edittext);
        cj_state = view.findViewById(R.id.cj_state_edittext);
        cj_col_index = view.findViewById(R.id.cj_col_index_edittext);
        cj_yearly_salary = view.findViewById(R.id.cj_yearly_salary_edittext);
        cj_yearly_bonus = view.findViewById(R.id.cj_yearly_bonus_edittext);
        cj_401k_match = view.findViewById(R.id.cj_401kmatch_edittext);
        cj_internet_stipend = view.findViewById(R.id.cj_internet_stipend_edittext);
        cj_accident_insurance = view.findViewById(R.id.cj_accident_insurance_edittext);
        cj_tuition_reimbursement = view.findViewById(R.id.cj_tuition_reimbursement_edittext);

        cj_cancel_button = view.findViewById(R.id.cj_cancel_button);
        cj_save_button = view.findViewById(R.id.cj_save_button);

        // Clearing the text fields or setting them to the current job
        jobViewModel.getCurrentJob().observe(getViewLifecycleOwner(), job -> {
            if(job == null) {
                clearFormFields();
                cj_save_button.setText("Save Current Job");
            }
            else {
                cj_title.setText(job.title);
                cj_company.setText(job.company);
                cj_city.setText(job.city);
                cj_state.setText(job.state);
                cj_col_index.setText(String.valueOf(job.costOfLivingIndex));
                cj_yearly_salary.setText(String.valueOf(job.yearlySalary));
                cj_yearly_bonus.setText(String.valueOf(job.yearlyBonus));
                cj_401k_match.setText(String.valueOf(job.match401k));
                cj_internet_stipend.setText(String.valueOf(job.internetStipend));
                cj_accident_insurance.setText(String.valueOf(job.accidentInsurance));
                cj_tuition_reimbursement.setText(String.valueOf(job.tuitionReimbursement));

                cj_save_button.setText("Update Current Job");
            }
        });

        // Adding save button functionality
        cj_save_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            if(!validateFormFields()) {
                Toast.makeText(requireContext(), "Please enter correct information", Toast.LENGTH_SHORT).show();
                return;
            }

            Job job = new Job();
            job.title = cj_title.getText().toString().trim();
            job.company = cj_company.getText().toString().trim();
            job.city= cj_city.getText().toString().trim();
            job.state = cj_state.getText().toString().trim();
            job.costOfLivingIndex = Integer.parseInt(cj_col_index.getText().toString().trim());
            job.yearlySalary = Double.parseDouble(cj_yearly_salary.getText().toString().trim());
            job.yearlyBonus = Double.parseDouble(cj_yearly_bonus.getText().toString().trim());
            job.match401k = Integer.parseInt(cj_401k_match.getText().toString().trim());
            job.internetStipend = Double.parseDouble(cj_internet_stipend.getText().toString().trim());
            job.accidentInsurance = Double.parseDouble(cj_accident_insurance.getText().toString().trim());
            job.tuitionReimbursement = Double.parseDouble(cj_tuition_reimbursement.getText().toString().trim());

            jobViewModel.saveCurrentJob(job);
            Toast.makeText(requireContext(), "Current job saved", Toast.LENGTH_SHORT).show();
            // return to main menu
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false);

        });

        // Adding cancel button functionality
        cj_cancel_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false);
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void clearFormFields() {
        cj_title.setText("");
        cj_company.setText("");
        cj_city.setText("");
        cj_state.setText("");
        cj_col_index.setText("");
        cj_yearly_salary.setText("");
        cj_yearly_bonus.setText("");
        cj_401k_match.setText("");
        cj_internet_stipend.setText("");
        cj_accident_insurance.setText("");
        cj_tuition_reimbursement.setText("");
    }

    private boolean validateFormFields() {
        // title
        String title = cj_title.getText().toString().trim();
        boolean okFlag = true;
        if(title.isEmpty()) {
            cj_title.setError("Title is required");
            okFlag = false;
        } else {
            cj_title.setError(null);
        }

        // company
        String company = cj_company.getText().toString().trim();
        if(company.isEmpty()) {
            cj_company.setError("Company is required");
            okFlag = false;
        }
        else {
            cj_company.setError(null);
        }

        // city
        String city = cj_city.getText().toString().trim();
        if(city.isEmpty()) {
            cj_city.setError("City is required");
            okFlag = false;
        }
        else {
            cj_city.setError(null);
        }

        // state
        String state = cj_state.getText().toString().trim();
        if(state.isEmpty()) {
            cj_state.setError("State is required");
            okFlag = false;
        }
        else {
            cj_state.setError(null);
        }

        // cost of living index
        String colIndex = cj_col_index.getText().toString().trim();
        if(colIndex.isEmpty()) {
            cj_col_index.setError("Cost of living index is required");
            okFlag = false;
        }
        else {
            try {
                int colIndexInt = Integer.parseInt(colIndex);
                if (colIndexInt < 1) {
                    cj_col_index.setError("Cost of living index must be a positive integer (>= 1)");
                    okFlag = false;
                }
                else {
                    cj_col_index.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_col_index.setError("Cost of living index must be a number");
                okFlag = false;
            }
        }

        // yearly salary
        String yearlySalary = cj_yearly_salary.getText().toString().trim();
        if(yearlySalary.isEmpty()) {
            cj_yearly_salary.setError("Yearly salary is required");
            okFlag = false;
        }
        else {
            try {
                double yearlySalaryDouble = Double.parseDouble(yearlySalary);
                if (yearlySalaryDouble <= 0) {
                    cj_yearly_salary.setError("Yearly salary must be greater than 0");
                    okFlag = false;
                }
                else {
                    cj_yearly_salary.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_yearly_salary.setError("Yearly salary must be a number");
                okFlag = false;
            }
        }

        // yearly bonus
        String yearlyBonus = cj_yearly_bonus.getText().toString().trim();
        if(yearlyBonus.isEmpty()) {
            cj_yearly_bonus.setError("Yearly bonus is required");
            okFlag = false;
        }
        else {
            try {
                double yearlyBonusDouble = Double.parseDouble(yearlyBonus);
                if (yearlyBonusDouble < 0) {
                    cj_yearly_bonus.setError("Yearly bonus must be greater than or equal to 0");
                    okFlag = false;
                }
                else {
                    cj_yearly_bonus.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_yearly_bonus.setError("Yearly bonus must be a number");
                okFlag = false;
            }
        }

        // 401k match
        String match401k = cj_401k_match.getText().toString().trim();
        if(match401k.isEmpty()) {
            cj_401k_match.setError("401K match is required");
            okFlag = false;
        }
        else {
            try {
                int match401kInt = Integer.parseInt(match401k);
                if (match401kInt < 0 || match401kInt > 6) {
                    cj_401k_match.setError("401K match must be between 0 and 6");
                    okFlag = false;
                }
                else {
                    cj_401k_match.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_401k_match.setError("401K match must be a number");
                okFlag = false;
            }
        }

        // internet stipend
        String internetStipend = cj_internet_stipend.getText().toString().trim();
        if(internetStipend.isEmpty()) {
            cj_internet_stipend.setError("Internet stipend is required");
            okFlag = false;
        }
        else {
            try {
                double internetStipendDouble = Double.parseDouble(internetStipend);
                if (internetStipendDouble < 0 || internetStipendDouble > 360) {
                    cj_internet_stipend.setError("Internet stipend must be between 0 and 360");
                    okFlag = false;
                }
                else {
                    cj_internet_stipend.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_internet_stipend.setError("Internet stipend must be a number");
                okFlag = false;
            }
        }

        // accident insurance
        String accidentInsurance = cj_accident_insurance.getText().toString().trim();
        if(accidentInsurance.isEmpty()) {
            cj_accident_insurance.setError("Accident insurance is required");
            okFlag = false;
        }
        else {
            try {
                double accidentInsuranceDouble = Double.parseDouble(accidentInsurance);
                if (accidentInsuranceDouble < 0 || accidentInsuranceDouble > 50000) {
                    cj_accident_insurance.setError("Accident insurance must be between 0 and 50000");
                    okFlag = false;
                }
                else {
                    cj_accident_insurance.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_accident_insurance.setError("Accident insurance must be a number");
                okFlag = false;
            }
        }

        // tuition reimbursement
        String tuitionReimbursement = cj_tuition_reimbursement.getText().toString().trim();
        if(tuitionReimbursement.isEmpty()) {
            cj_tuition_reimbursement.setError("Tuition reimbursement is required");
            okFlag = false;
        }
        else {
            try {
                double tuitionReimbursementDouble = Double.parseDouble(tuitionReimbursement);
                if (tuitionReimbursementDouble < 0 || tuitionReimbursementDouble > 12000) {
                    cj_tuition_reimbursement.setError("Tuition reimbursement must be between 0 and 12000");
                    okFlag = false;
                }
                else {
                    cj_tuition_reimbursement.setError(null);
                }
            } catch (NumberFormatException e) {
                cj_tuition_reimbursement.setError("Tuition reimbursement must be a number");
                okFlag = false;
            }
        }

        return okFlag;
    }
}