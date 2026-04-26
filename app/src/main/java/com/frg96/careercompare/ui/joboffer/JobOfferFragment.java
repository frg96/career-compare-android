package com.frg96.careercompare.ui.joboffer;

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

public class JobOfferFragment extends Fragment {
    private JobViewModel jobViewModel;

    EditText jo_title;
    EditText jo_company;
    EditText jo_city;
    EditText jo_state;
    EditText jo_col_index;
    EditText jo_yearly_salary;
    EditText jo_yearly_bonus;
    EditText jo_401k_match;
    EditText jo_internet_stipend;
    EditText jo_accident_insurance;
    EditText jo_tuition_reimbursement;

    Button jo_cancel_button;
    Button jo_add_button;
    Button jo_compare_button;

    private boolean isAddMode = true;

    // Cache latest job IDs from LiveData
    private Long latestCurrentJobId;
    private Long latestLastSavedOfferId;

    public JobOfferFragment() {
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
        View view = inflater.inflate(R.layout.fragment_job_offer, container, false);

        jobViewModel = new ViewModelProvider(requireActivity()).get(JobViewModel.class);

        jobViewModel.getCurrentJobId().observe(getViewLifecycleOwner(), currentJobId -> {
            latestCurrentJobId = currentJobId;
        });

        jobViewModel.getLastSavedOfferId().observe(getViewLifecycleOwner(), lastSavedOfferId -> {
            latestLastSavedOfferId = lastSavedOfferId;
        });

        jo_title = view.findViewById(R.id.jo_title_edittext);
        jo_company = view.findViewById(R.id.jo_company_edittext);
        jo_city = view.findViewById(R.id.jo_city_edittext);
        jo_state = view.findViewById(R.id.jo_state_edittext);
        jo_col_index = view.findViewById(R.id.jo_col_index_edittext);
        jo_yearly_salary = view.findViewById(R.id.jo_yearly_salary_edittext);
        jo_yearly_bonus = view.findViewById(R.id.jo_yearly_bonus_edittext);
        jo_401k_match = view.findViewById(R.id.jo_401kmatch_edittext);
        jo_internet_stipend = view.findViewById(R.id.jo_internet_stipend_edittext);
        jo_accident_insurance = view.findViewById(R.id.jo_accident_insurance_edittext);
        jo_tuition_reimbursement = view.findViewById(R.id.jo_tuition_reimbursement_edittext);


        jo_cancel_button = view.findViewById(R.id.jo_cancel_button);
        jo_add_button = view.findViewById(R.id.jo_add_button);
        jo_compare_button = view.findViewById(R.id.jo_compare_button);

        jo_add_button.setText("Add Job Offer");

        // Clearing the text fields
        clearFormFields();


        // Enabling/Disabling the compare button based on whether there is a current job and at least one job offer
        jo_compare_button.setEnabled(false);

        jobViewModel.canCompareFromJobOffers().observe(getViewLifecycleOwner(), enabled -> {
            jo_compare_button.setEnabled(Boolean.TRUE.equals(enabled));
        });


        // Adding Add Job Offer button functionality
        jo_add_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            if(isAddMode) {
                if (!validateFormFields()) {
                    Toast.makeText(requireContext(), "Please enter correct information", Toast.LENGTH_SHORT).show();
                    return;
                }

                Job job = new Job();
                job.title = jo_title.getText().toString().trim();
                job.company = jo_company.getText().toString().trim();
                job.city = jo_city.getText().toString().trim();
                job.state = jo_state.getText().toString().trim();
                job.costOfLivingIndex = Integer.parseInt(jo_col_index.getText().toString().trim());
                job.yearlySalary = Double.parseDouble(jo_yearly_salary.getText().toString().trim());
                job.yearlyBonus = Double.parseDouble(jo_yearly_bonus.getText().toString().trim());
                job.match401k = Integer.parseInt(jo_401k_match.getText().toString().trim());
                job.internetStipend = Double.parseDouble(jo_internet_stipend.getText().toString().trim());
                job.accidentInsurance = Double.parseDouble(jo_accident_insurance.getText().toString().trim());
                job.tuitionReimbursement = Double.parseDouble(jo_tuition_reimbursement.getText().toString().trim());

                jobViewModel.insertJob(job);
                Toast.makeText(requireContext(), "Added Job Offer", Toast.LENGTH_SHORT).show();
                isAddMode = false;
                jo_add_button.setText("Add Another Job Offer");
            }
            else {
                clearFormFields();
                jo_title.requestFocus();
                Toast.makeText(requireContext(),"Cleared fields. Enter new job offer", Toast.LENGTH_SHORT).show();
                isAddMode = true;
                jo_add_button.setText("Add Job Offer");
            }

        });

        // Adding Cancel button functionality
        jo_cancel_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false);
        });

        // Adding Compare button functionality
        jo_compare_button.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            Long currentJobId = latestCurrentJobId;
            Long lastSavedOfferId = latestLastSavedOfferId;
            if(currentJobId != null && lastSavedOfferId != null) {
                JobOfferFragmentDirections.ActionJobOfferFragmentToCompareTwoJobsFragment action =
                        JobOfferFragmentDirections.actionJobOfferFragmentToCompareTwoJobsFragment(
                                currentJobId,
                                lastSavedOfferId,
                                "jobOfferFragment"
                        );
                NavHostFragment.findNavController(JobOfferFragment.this).navigate(action);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void clearFormFields() {
        jo_title.setText("");
        jo_company.setText("");
        jo_city.setText("");
        jo_state.setText("");
        jo_col_index.setText("");
        jo_yearly_salary.setText("");
        jo_yearly_bonus.setText("");
        jo_401k_match.setText("");
        jo_internet_stipend.setText("");
        jo_accident_insurance.setText("");
        jo_tuition_reimbursement.setText("");
    }

    private boolean validateFormFields() {
        // title
        String title = jo_title.getText().toString().trim();
        boolean okFlag = true;
        if(title.isEmpty()) {
            jo_title.setError("Title is required");
            okFlag = false;
        } else {
            jo_title.setError(null);
        }

        // company
        String company = jo_company.getText().toString().trim();
        if(company.isEmpty()) {
            jo_company.setError("Company is required");
            okFlag = false;
        }
        else {
            jo_company.setError(null);
        }

        // city
        String city = jo_city.getText().toString().trim();
        if(city.isEmpty()) {
            jo_city.setError("City is required");
            okFlag = false;
        }
        else {
            jo_city.setError(null);
        }

        // state
        String state = jo_state.getText().toString().trim();
        if(state.isEmpty()) {
            jo_state.setError("State is required");
            okFlag = false;
        }
        else {
            jo_state.setError(null);
        }

        // cost of living index
        String colIndex = jo_col_index.getText().toString().trim();
        if(colIndex.isEmpty()) {
            jo_col_index.setError("Cost of living index is required");
            okFlag = false;
        }
        else {
            try {
                int colIndexInt = Integer.parseInt(colIndex);
                if (colIndexInt < 1) {
                    jo_col_index.setError("Cost of living index must be a positive integer (>= 1)");
                    okFlag = false;
                }
                else {
                    jo_col_index.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_col_index.setError("Cost of living index must be a number");
                okFlag = false;
            }
        }

        // yearly salary
        String yearlySalary = jo_yearly_salary.getText().toString().trim();
        if(yearlySalary.isEmpty()) {
            jo_yearly_salary.setError("Yearly salary is required");
            okFlag = false;
        }
        else {
            try {
                double yearlySalaryDouble = Double.parseDouble(yearlySalary);
                if (yearlySalaryDouble <= 0) {
                    jo_yearly_salary.setError("Yearly salary must be greater than 0");
                    okFlag = false;
                }
                else {
                    jo_yearly_salary.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_yearly_salary.setError("Yearly salary must be a number");
                okFlag = false;
            }
        }

        // yearly bonus
        String yearlyBonus = jo_yearly_bonus.getText().toString().trim();
        if(yearlyBonus.isEmpty()) {
            jo_yearly_bonus.setError("Yearly bonus is required");
            okFlag = false;
        }
        else {
            try {
                double yearlyBonusDouble = Double.parseDouble(yearlyBonus);
                if (yearlyBonusDouble < 0) {
                    jo_yearly_bonus.setError("Yearly bonus must be greater than or equal to 0");
                    okFlag = false;
                }
                else {
                    jo_yearly_bonus.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_yearly_bonus.setError("Yearly bonus must be a number");
                okFlag = false;
            }
        }

        // 401k match
        String match401k = jo_401k_match.getText().toString().trim();
        if(match401k.isEmpty()) {
            jo_401k_match.setError("401K match is required");
            okFlag = false;
        }
        else {
            try {
                int match401kInt = Integer.parseInt(match401k);
                if (match401kInt < 0 || match401kInt > 6) {
                    jo_401k_match.setError("401K match must be between 0 and 6");
                    okFlag = false;
                }
                else {
                    jo_401k_match.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_401k_match.setError("401K match must be a number");
                okFlag = false;
            }
        }

        // internet stipend
        String internetStipend = jo_internet_stipend.getText().toString().trim();
        if(internetStipend.isEmpty()) {
            jo_internet_stipend.setError("Internet stipend is required");
            okFlag = false;
        }
        else {
            try {
                double internetStipendDouble = Double.parseDouble(internetStipend);
                if (internetStipendDouble < 0 || internetStipendDouble > 360) {
                    jo_internet_stipend.setError("Internet stipend must be between 0 and 360");
                    okFlag = false;
                }
                else {
                    jo_internet_stipend.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_internet_stipend.setError("Internet stipend must be a number");
                okFlag = false;
            }
        }

        // accident insurance
        String accidentInsurance = jo_accident_insurance.getText().toString().trim();
        if(accidentInsurance.isEmpty()) {
            jo_accident_insurance.setError("Accident insurance is required");
            okFlag = false;
        }
        else {
            try {
                double accidentInsuranceDouble = Double.parseDouble(accidentInsurance);
                if (accidentInsuranceDouble < 0 || accidentInsuranceDouble > 50000) {
                    jo_accident_insurance.setError("Accident insurance must be between 0 and 50000");
                    okFlag = false;
                }
                else {
                    jo_accident_insurance.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_accident_insurance.setError("Accident insurance must be a number");
                okFlag = false;
            }
        }

        // tuition reimbursement
        String tuitionReimbursement = jo_tuition_reimbursement.getText().toString().trim();
        if(tuitionReimbursement.isEmpty()) {
            jo_tuition_reimbursement.setError("Tuition reimbursement is required");
            okFlag = false;
        }
        else {
            try {
                double tuitionReimbursementDouble = Double.parseDouble(tuitionReimbursement);
                if (tuitionReimbursementDouble < 0 || tuitionReimbursementDouble > 12000) {
                    jo_tuition_reimbursement.setError("Tuition reimbursement must be between 0 and 12000");
                    okFlag = false;
                }
                else {
                    jo_tuition_reimbursement.setError(null);
                }
            } catch (NumberFormatException e) {
                jo_tuition_reimbursement.setError("Tuition reimbursement must be a number");
                okFlag = false;
            }
        }

        return okFlag;
    }
}