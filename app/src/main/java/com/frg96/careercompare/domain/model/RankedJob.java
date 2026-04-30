package com.frg96.careercompare.domain.model;

import com.frg96.careercompare.data.entity.Job;

public class RankedJob {
    public Job job;

    public double adjustedYearlySalary;

    public double adjustedYearlyBonus;

    public double jobScore;

    public RankedJob(Job job, double adjustedYearlySalary, double adjustedYearlyBonus, double jobScore) {
        this.job = job;
        this.adjustedYearlySalary = adjustedYearlySalary;
        this.adjustedYearlyBonus = adjustedYearlyBonus;
        this.jobScore = jobScore;
    }
}
