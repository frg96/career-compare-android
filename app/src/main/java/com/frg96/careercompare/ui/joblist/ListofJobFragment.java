package com.frg96.careercompare.ui.joblist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frg96.careercompare.R;
import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.domain.model.RankedJob;
import com.frg96.careercompare.domain.util.JobRanker;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListofJobFragment extends Fragment implements JobListAdaptor.OnJobClickListener  {
    private JobViewModel jobViewModel;

    private List<Job> jobList = new ArrayList<>();

    private Job currentJob;
    private List<Job> jobOffers = new ArrayList<>();

    private JobListAdaptor adapter;
    private RecyclerView recyclerView;

    private Long currentJobID = null;

    private Weights currentWeights;

    private List<RankedJob> rankedjobs = new ArrayList<>();

    private Long firstSelectedId = null;
    private Long secondSelectedId = null;

    private ExtendedFloatingActionButton compareFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firstSelectedId = null;
        secondSelectedId = null;
        return inflater.inflate(R.layout.fragment_job_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.job_list);

        adapter = new JobListAdaptor(requireContext(), rankedjobs);
        adapter.setOnJobClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerView.ItemAnimator ia = recyclerView.getItemAnimator();
        if (ia instanceof androidx.recyclerview.widget.SimpleItemAnimator) {
            ((androidx.recyclerview.widget.SimpleItemAnimator) ia).setSupportsChangeAnimations(false);
        }

        jobViewModel = new ViewModelProvider(requireActivity()).get(JobViewModel.class);

        compareFab = view.findViewById(R.id.compare_fab);
        compareFab.hide(); //initial state

        compareFab.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            var action = ListofJobFragmentDirections
                    .actionListofJobFragmentToCompareTwoJobsFragment(firstSelectedId, secondSelectedId, "ListofJobFragment");
            NavHostFragment.findNavController(ListofJobFragment.this).navigate(action);
        });

        jobViewModel.getCurrentJob().observe(getViewLifecycleOwner(), new Observer<Job>() {
            @Override
            public void onChanged(Job job) {
                currentJob = job;
                mergeJobsAndUpdateUI();
            }
        });

        jobViewModel.getJobOffers().observe(getViewLifecycleOwner(), new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> jobs) {
                jobOffers = jobs != null ? jobs : new ArrayList<Job>();
                mergeJobsAndUpdateUI();
            }
        });

        jobViewModel.getCurrentJobId().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long id) {
                currentJobID = id;
                if (adapter != null) adapter.setCurrentJobId(currentJobID);
                mergeJobsAndUpdateUI();
            }
        });

        jobViewModel.getCurrentWeights().observe(getViewLifecycleOwner(), new Observer<Weights>() {
            @Override
            public void onChanged(Weights weights) {
                currentWeights = weights;
                mergeJobsAndUpdateUI();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void mergeJobsAndUpdateUI(){

        jobList.clear();

        if(currentJob!= null){
            jobList.add(currentJob);
        }

        if (jobOffers != null && !jobOffers.isEmpty()) {
            jobList.addAll(jobOffers);
        }

        rankedjobs = JobRanker.getSortedRankedJobs(jobList, currentWeights);
        Log.d("Listofjobfragment", "-> ranedjobs values are: "+ rankedjobs);
        //must replace the data since the initial is null
        adapter.replaceData(rankedjobs);
    }

    @Override
    public void onJobClicked(long jobId) {
        // Deselect if it's already selected
        if (firstSelectedId != null && firstSelectedId.equals(jobId)) {
            // Remove first; shift second up (if any)
            firstSelectedId = secondSelectedId;
            secondSelectedId = null;
        } else if (secondSelectedId != null && secondSelectedId.equals(jobId)) {
            // Remove second
            secondSelectedId = null;
        } else if (firstSelectedId == null) {
            // Pick first
            firstSelectedId = jobId;
        } else if (secondSelectedId == null) {
            // Pick second
            secondSelectedId = jobId;
        } else {
            // Both filled and a new one tapped: shift
            firstSelectedId = secondSelectedId;
            secondSelectedId = jobId;
        }

        pushSelectionToAdapter();
        updateCompareFab();
    }

    private void updateCompareFab() {
        boolean twoSelected = (firstSelectedId != null && secondSelectedId != null
                && !firstSelectedId.equals(secondSelectedId));

        if (twoSelected) {
            compareFab.show();
            compareFab.extend();
        } else {
            compareFab.hide(); // if user de-select the job.
        }
    }

    private void pushSelectionToAdapter() {
        Set<Long> ids = new HashSet<>();
        if (firstSelectedId != null)  ids.add(firstSelectedId);
        if (secondSelectedId != null) ids.add(secondSelectedId);
        adapter.setSelectedIds(ids);
    }
}




