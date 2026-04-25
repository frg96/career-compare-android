package com.frg96.careercompare.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.frg96.careercompare.data.dao.AppStateDAO;
import com.frg96.careercompare.data.dao.JobDAO;
import com.frg96.careercompare.data.dao.WeightsDAO;
import com.frg96.careercompare.data.entity.AppState;
import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Job.class, AppState.class, Weights.class},
        version = 2,
        exportSchema = false
)
public abstract class JobCompareRoomDatabase extends RoomDatabase {

    public abstract JobDAO jobDao();

    public abstract WeightsDAO weightsDao();

    public abstract AppStateDAO appStateDao();

    private static volatile JobCompareRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutorService =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static JobCompareRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JobCompareRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JobCompareRoomDatabase.class, "job_compare_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
