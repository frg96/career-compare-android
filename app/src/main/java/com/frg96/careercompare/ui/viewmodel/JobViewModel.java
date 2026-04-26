package com.frg96.careercompare.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.data.repository.JobCompareRepository;

import java.util.List;

public class JobViewModel extends AndroidViewModel {

    private final JobCompareRepository repository;

    public JobViewModel(@NonNull Application application) {
        super(application);
        repository = new JobCompareRepository(application);
    }

    public LiveData<Job> getCurrentJob() {
        return repository.getCurrentJob();
    }

    public LiveData<Weights> getCurrentWeights() {
        return repository.getCurrentWeights();
    }
    //add update weights functionality
    public LiveData<List<Job>> getJobOffers() {
        return repository.getJobOffers();
    }

    public LiveData<Boolean> canCompareFromJobOffers() {
        return repository.canCompareFromJobOffer();
    }

    public LiveData<Boolean> canCompareFromMainMenu() {
        return repository.canCompareFromMainMenu();
    }

    public LiveData<Job> getJobById(long id) {
        return repository.getJobById(id);
    }

    public void insertJob(Job job) {
        repository.insertJob(job);
    }

    public void updateJob(Job job) {
        repository.updateJob(job);
    }

    public void deleteJob(Job job) {
        repository.deleteJob(job);
    }

    public void saveCurrentJob(Job job) {
        repository.saveCurrentJob(job);
    }

    public void setCurrentJob(long jobId) {
        repository.setCurrentJob(jobId);
    }

    public void clearCurrentJob() {
        repository.clearCurrentJob();
    }

    public LiveData<Long> getCurrentJobId() {
        return repository.getCurrentJobId();
    }

    public LiveData<Long> getLastSavedOfferId() {
        return repository.getLastJobOfferId();
    }


    public void saveCurrentWeights(Weights weights) {
        repository.saveCurrentWeights(weights);
    }
}
