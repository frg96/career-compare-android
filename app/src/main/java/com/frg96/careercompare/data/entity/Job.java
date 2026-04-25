package com.frg96.careercompare.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "jobs")
public class Job {
//    @Ignore
//    public Job() {}

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "job_id")
    public Long jobId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "company")
    public String company;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "cost_of_living_index")
    public Integer costOfLivingIndex;

    @ColumnInfo(name = "yearly_salary")
    public Double yearlySalary;

    @ColumnInfo(name = "yearly_bonus")
    public Double yearlyBonus;

    @ColumnInfo(name = "match_401k")
    public Integer match401k;

    @ColumnInfo(name = "internet_stipend")
    public Double internetStipend;

    @ColumnInfo(name = "accident_insurance")
    public Double accidentInsurance;

    @ColumnInfo(name = "tuition_reimbursement")
    public Double tuitionReimbursement;

}

