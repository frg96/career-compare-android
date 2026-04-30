package com.frg96.careercompare.ui.comparison;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.frg96.careercompare.R;
import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.domain.model.RankedJob;
import com.frg96.careercompare.domain.util.JobRanker;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;

import java.text.DecimalFormat;

public class CompareTwoJobsFragment extends Fragment {
    private Long jobIDLeft;
    private Long jobIDRight;
    private String sourceFragment;

    private Job jobLeft;
    private Job jobRight;
    private Weights weights;



    private JobViewModel jobViewModel;

    TextView cmpTblHeaderLeft;
    TextView cmpTblTitleLeft;
    TextView cmpTblCompanyLeft;
    TextView cmpTblLocationLeft;
    TextView cmpTblAysLeft;
    TextView cmpTblAybLeft;
    TextView cmpTbl401kLeft;
    TextView cmpTblIsLeft;
    TextView cmpTblAiLeft;
    TextView cmpTblTrLeft;
    TextView cmpTblJsLeft;

    TextView cmpTblHeaderRight;
    TextView cmpTblTitleRight;
    TextView cmpTblCompanyRight;
    TextView cmpTblLocationRight;
    TextView cmpTblAysRight;
    TextView cmpTblAybRight;
    TextView cmpTbl401kRight;
    TextView cmpTblIsRight;
    TextView cmpTblAiRight;
    TextView cmpTblTrRight;
    TextView cmpTblJsRight;

    Button cmp_tbl_main_menu_button;

    Button cmp_tbl_new_compare_button;

    public CompareTwoJobsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_compare_two_jobs, container, false);

        cmpTblHeaderLeft = v.findViewById(R.id.cmp_tbl_header_left);
        cmpTblTitleLeft = v.findViewById(R.id.cmp_tbl_title_left);
        cmpTblCompanyLeft = v.findViewById(R.id.cmp_tbl_company_left);
        cmpTblLocationLeft = v.findViewById(R.id.cmp_tbl_location_left);
        cmpTblAysLeft = v.findViewById(R.id.cmp_tbl_ays_left);
        cmpTblAybLeft = v.findViewById(R.id.cmp_tbl_ayb_left);
        cmpTbl401kLeft = v.findViewById(R.id.cmp_tbl_401k_left);
        cmpTblIsLeft = v.findViewById(R.id.cmp_tbl_is_left);
        cmpTblAiLeft = v.findViewById(R.id.cmp_tbl_ai_left);
        cmpTblTrLeft = v.findViewById(R.id.cmp_tbl_tr_left);
        cmpTblJsLeft = v.findViewById(R.id.cmp_tbl_js_left);

        cmpTblHeaderRight = v.findViewById(R.id.cmp_tbl_header_right);
        cmpTblTitleRight = v.findViewById(R.id.cmp_tbl_title_right);
        cmpTblCompanyRight = v.findViewById(R.id.cmp_tbl_company_right);
        cmpTblLocationRight = v.findViewById(R.id.cmp_tbl_location_right);
        cmpTblAysRight = v.findViewById(R.id.cmp_tbl_ays_right);
        cmpTblAybRight = v.findViewById(R.id.cmp_tbl_ayb_right);
        cmpTbl401kRight = v.findViewById(R.id.cmp_tbl_401k_right);
        cmpTblIsRight = v.findViewById(R.id.cmp_tbl_is_right);
        cmpTblAiRight = v.findViewById(R.id.cmp_tbl_ai_right);
        cmpTblTrRight = v.findViewById(R.id.cmp_tbl_tr_right);
        cmpTblJsRight = v.findViewById(R.id.cmp_tbl_js_right);

        cmp_tbl_main_menu_button = v.findViewById(R.id.cmp_tbl_main_menu_button);
        cmp_tbl_new_compare_button = v.findViewById(R.id.cmp_tbl_new_compare_button);

        cmp_tbl_main_menu_button.setOnClickListener(view -> {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            NavHostFragment.findNavController(this).popBackStack(R.id.mainMenuFragment, false); // return to main menu
        });

        cmp_tbl_new_compare_button.setOnClickListener(view -> {
            view.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            NavHostFragment.findNavController(this).popBackStack(R.id.listofJobFragment, false); // return to list of job fragment
        });

        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);

        CompareTwoJobsFragmentArgs args = CompareTwoJobsFragmentArgs.fromBundle(getArguments());
        jobIDLeft = args.getJobIdLeft();
        jobIDRight = args.getJobIdRight();
        sourceFragment = args.getSourceFragment();

        if(jobIDLeft != null && jobIDRight != null && sourceFragment != null) {

            if(sourceFragment.equals("jobOfferFragment")) {
                // if this fragment is loaded from the job offer fragment
                cmpTblHeaderLeft.setText("Current Job");
                cmpTblHeaderRight.setText("Last Saved Job Offer");
            }
            else {
                // if this fragment is loaded from any other fragment
                cmpTblHeaderLeft.setText("Job 1");
                cmpTblHeaderRight.setText("Job 2");

                cmp_tbl_new_compare_button.setVisibility(View.VISIBLE);
            }

            jobViewModel.getJobById(jobIDLeft).observe(getViewLifecycleOwner(), job -> {
                this.jobLeft = job;
                tryPopulateTable();
            });

            jobViewModel.getJobById(jobIDRight).observe(getViewLifecycleOwner(), job -> {
                this.jobRight = job;
                tryPopulateTable();
            });

            jobViewModel.getCurrentWeights().observe(getViewLifecycleOwner(), weights -> {
                this.weights = weights;
                tryPopulateTable();
            });
        }
    }

    private void tryPopulateTable() {
        if(jobLeft == null || jobRight == null || weights == null) {
            return; //wait until all data is loaded
        }

        RankedJob rankedJobLeft = JobRanker.getRankedJob(jobLeft, weights);
        RankedJob rankedJobRight = JobRanker.getRankedJob(jobRight, weights);


        cmpTblTitleLeft.setText(rankedJobLeft.job.title);
        cmpTblCompanyLeft.setText(rankedJobLeft.job.company);
        cmpTblLocationLeft.setText(rankedJobLeft.job.city + ", " + rankedJobLeft.job.state);
        cmpTblAysLeft.setText(withTwoDecimal(rankedJobLeft.adjustedYearlySalary));
        cmpTblAybLeft.setText(withTwoDecimal(rankedJobLeft.adjustedYearlyBonus));
        cmpTbl401kLeft.setText(String.valueOf(rankedJobLeft.job.match401k));
        cmpTblIsLeft.setText(withTwoDecimal(rankedJobLeft.job.internetStipend));
        cmpTblAiLeft.setText(withTwoDecimal(rankedJobLeft.job.accidentInsurance));
        cmpTblTrLeft.setText(withTwoDecimal(rankedJobLeft.job.tuitionReimbursement));
        cmpTblJsLeft.setText(withTwoDecimal(rankedJobLeft.jobScore));

        cmpTblTitleRight.setText(rankedJobRight.job.title);
        cmpTblCompanyRight.setText(rankedJobRight.job.company);
        cmpTblLocationRight.setText(rankedJobRight.job.city + ", " + jobRight.state);
        cmpTblAysRight.setText(withTwoDecimal(rankedJobRight.adjustedYearlySalary));
        cmpTblAybRight.setText(withTwoDecimal(rankedJobRight.adjustedYearlyBonus));
        cmpTbl401kRight.setText(String.valueOf(rankedJobRight.job.match401k));
        cmpTblIsRight.setText(withTwoDecimal(rankedJobRight.job.internetStipend));
        cmpTblAiRight.setText(withTwoDecimal(rankedJobRight.job.accidentInsurance));
        cmpTblTrRight.setText(withTwoDecimal(rankedJobRight.job.tuitionReimbursement));
        cmpTblJsRight.setText(withTwoDecimal(rankedJobRight.jobScore));



    }

    private String withTwoDecimal(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }
}