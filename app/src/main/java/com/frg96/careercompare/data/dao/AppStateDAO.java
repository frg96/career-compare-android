package com.frg96.careercompare.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.frg96.careercompare.data.entity.AppState;

@Dao
public interface AppStateDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIfMissing(AppState s);

    @Update
    void update(AppState s);

    @Query("SELECT * FROM app_state WHERE app_id = 1 LIMIT 1")
    LiveData<AppState> getLive();

    @Query("SELECT current_job_id FROM app_state WHERE app_id = 1 LIMIT 1")
    LiveData<Long> getCurrentJobId();

    @Query("SELECT last_job_offer_id FROM app_state WHERE app_id = 1 LIMIT 1")
    LiveData<Long> getLastJobOfferId();

    @Query("SELECT * FROM app_state WHERE app_id = 1 LIMIT 1")
    AppState getBlocking();

    @Query("UPDATE app_state SET current_job_id = :jobId WHERE app_id = 1")
    void setCurrentJobId(Long jobId);

    @Query("UPDATE app_state SET last_job_offer_id = :jobId WHERE app_id = 1")
    void setLastJobOfferId(Long jobId);
}
