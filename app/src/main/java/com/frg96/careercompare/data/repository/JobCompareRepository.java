package com.frg96.careercompare.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.frg96.careercompare.data.dao.AppStateDAO;
import com.frg96.careercompare.data.dao.JobDAO;
import com.frg96.careercompare.data.dao.WeightsDAO;
import com.frg96.careercompare.data.database.JobCompareRoomDatabase;
import com.frg96.careercompare.data.entity.AppState;
import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;

import java.util.List;

public class JobCompareRepository {

    private final JobDAO jobDao;
    private final WeightsDAO weightsDao;
    private final AppStateDAO appStateDao;

    private final LiveData<Job> currentJob;
    private final LiveData<List<Job>> jobOffers;
    private final LiveData<Weights> currentWeights;
    private final LiveData<AppState> liveAppState;

    private final MediatorLiveData<Boolean> canCompareFromJobOffer = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> canCompareFromMainMenu = new MediatorLiveData<>();


    public JobCompareRepository(Application application) {
        JobCompareRoomDatabase db = JobCompareRoomDatabase.getDatabase(application);
        this.jobDao = db.jobDao();
        this.appStateDao = db.appStateDao();
        this.currentJob = jobDao.getCurrentJob();
        this.jobOffers = jobDao.getJobOffers();
        //need this for weights
        this.weightsDao = db.weightsDao();
        this.currentWeights = weightsDao.getWeights();
        this.liveAppState = appStateDao.getLive();

        canCompareFromJobOffer.setValue(false);
        canCompareFromJobOffer.addSource(currentJob, job -> updateCanCompareFromJobOffer());
        canCompareFromJobOffer.addSource(jobOffers, jobs -> updateCanCompareFromJobOffer());
        canCompareFromJobOffer.addSource(currentWeights, weights -> updateCanCompareFromJobOffer());

        canCompareFromMainMenu.setValue(false);
        canCompareFromMainMenu.addSource(currentJob, job -> updateCanCompareFromMainMenu());
        canCompareFromMainMenu.addSource(jobOffers, jobs -> updateCanCompareFromMainMenu());
        canCompareFromMainMenu.addSource(currentWeights, weights -> updateCanCompareFromMainMenu());


        JobCompareRoomDatabase.databaseExecutorService.execute(() -> {
            AppState s = new AppState();
            appStateDao.insertIfMissing(s);

            Weights w = new Weights();
            weightsDao.insertIfMissing(w);
        });
    }

    private void updateCanCompareFromJobOffer() {
        Job j = currentJob.getValue();
        List<Job> offers = jobOffers.getValue();
        Weights w = currentWeights.getValue();

        boolean enabled = (j != null) && (offers != null && !offers.isEmpty()) && (w != null);
        Boolean prev = canCompareFromJobOffer.getValue();
        if (prev == null || prev != enabled) {
            canCompareFromJobOffer.setValue(enabled);
        }
    }

    private void updateCanCompareFromMainMenu() {
        Job cj = currentJob.getValue();
        List<Job> offers = jobOffers.getValue();
        Weights w = currentWeights.getValue();

        boolean enabled = (
                (cj == null && offers != null && offers.size() >= 2) || // when no current job and at least 2 offers
                (cj != null && offers != null && !offers.isEmpty())     // when current job and at least 1 offer
        ) && (w != null);
        Boolean prev = canCompareFromMainMenu.getValue();
        if (prev == null || prev != enabled) {
            canCompareFromMainMenu.setValue(enabled);
        }
    }


    // reads
    public LiveData<Job> getCurrentJob() { return currentJob; }

    public LiveData<Weights> getCurrentWeights() { return currentWeights; }
    public LiveData<List<Job>> getJobOffers() { return jobOffers; }
    public LiveData<AppState> getLiveAppState() { return liveAppState; }

    public LiveData<Boolean> canCompareFromJobOffer() { return canCompareFromJobOffer; }

    public LiveData<Boolean> canCompareFromMainMenu() { return canCompareFromMainMenu; }

    public LiveData<Job> getJobById(long id) { return jobDao.getJobById(id); }


    // writes
    public void insertJob(Job job) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> {
            Long id = jobDao.insert(job);
            appStateDao.setLastJobOfferId(id);
        });
    }

    public void updateJob(Job job) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> jobDao.update(job));
    }

    public void deleteJob(Job job) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> {
            jobDao.delete(job);
            // If the deleted job was current, Room FK onDelete=SET_NULL will clear currentJobId automatically
            // because of the foreign key on AppState.
        });
    }

    public void saveCurrentJob(Job formJob) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> {
            AppState appState = appStateDao.getBlocking();
            if(appState == null || appState.currentJobId == null) {
                Long id = jobDao.insert(formJob);
                appStateDao.setCurrentJobId(id);
            }
            else {
                formJob.jobId = appState.currentJobId;
                jobDao.update(formJob);
            }
        });
            }

    public void setCurrentJob(long jobId) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> appStateDao.setCurrentJobId(jobId));
    }

    public void clearCurrentJob() {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> appStateDao.setCurrentJobId(null));
    }

    public LiveData<Long> getCurrentJobId() { return appStateDao.getCurrentJobId(); }
    public LiveData<Long> getLastJobOfferId() { return appStateDao.getLastJobOfferId(); }



    public void saveCurrentWeights(Weights weights) {
        JobCompareRoomDatabase.databaseExecutorService.execute(() -> weightsDao.insert(weights));
    }
}
