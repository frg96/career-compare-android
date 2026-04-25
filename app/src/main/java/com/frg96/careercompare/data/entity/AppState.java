package com.frg96.careercompare.data.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "app_state",
        foreignKeys = @ForeignKey(
                entity = Job.class,
                parentColumns = "job_id",
                childColumns = "current_job_id",
                onDelete = ForeignKey.SET_NULL
        ),
        indices = {@Index("current_job_id")}
)
public class AppState {
    @PrimaryKey
    @ColumnInfo(name = "app_id")
    public int appId = 1;
    
    @Nullable
    @ColumnInfo(name = "current_job_id")
    public Long currentJobId;

    @Nullable
    @ColumnInfo(name = "last_job_offer_id")
    public Long lastJobOfferId;
}
