
package com.frg96.careercompare.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.frg96.careercompare.data.entity.Weights;
//import androidx.room.Update;

//import java.util.List;

@Dao
public interface WeightsDAO {
    //add constraints
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weights weights);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIfMissing(Weights weights);

    @Delete
    void delete(Weights weights);

    @Query("SELECT * FROM weights_table WHERE weights_id = 1")
    LiveData<Weights> getWeights();


//    @Update
//    void update(Weights weights);

//    @Query("DELETE FROM weights_table")
//    void deleteAll();

//    @Query("SELECT * FROM jobs ORDER BY job_id DESC")
//    LiveData<List<Job>> getAll();

//    @Query("SELECT * FROM jobs WHERE job_id = :id LIMIT 1")
//    LiveData<Job> getJobById(Long id);

    // blocking helper for repository logic
//    @Query("SELECT * FROM jobs WHERE job_id = :id LIMIT 1")
//    Job getJobByIdBlocking(Long id);

//    @Query("SELECT * FROM jobs WHERE job_id = (SELECT current_job_id FROM app_state where app_id = 1)")
//    LiveData<Job> getCurrentJob();

//    @Query("SELECT * FROM jobs WHERE job_id != (SELECT current_job_id FROM app_state WHERE app_id = 1) OR " +
//            "(SELECT current_job_id FROM app_state WHERE app_id = 1) IS NULL " +
//            "ORDER BY job_id DESC")
//    LiveData<List<Job>> getJobOffers();


}
