package com.frg96.careercompare.domain.util;

import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.domain.model.RankedJob;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JobRanker {
    public static double adjustForCostOfLiving(double amount, int colIndex) {
        return amount * (100.0 / colIndex);
    }

    public static double getJobScore(Job job, Weights weights) {
        double adjustedYearlySalary = adjustForCostOfLiving(job.yearlySalary, job.costOfLivingIndex);
        double adjustedYearlyBonus = adjustForCostOfLiving(job.yearlyBonus, job.costOfLivingIndex);

        double matched401kValue = adjustedYearlySalary * (job.match401k / 100.0);

        // temporary use just for jobscore calculation
        Weights newWeights = new Weights(weights);

        double sumOfWeights = newWeights.yearlySalaryWeight
                + newWeights.yearlyBonusWeight
                + newWeights.match401kWeight
                + newWeights.internetStipendWeight
                + newWeights.accidentInsuranceWeight
                + newWeights.tuitionReimbursementWeight;

        if (sumOfWeights == 0) {
            // default everything to 1 when no weights are set
            newWeights.yearlySalaryWeight = 1;
            newWeights.yearlyBonusWeight = 1;
            newWeights.match401kWeight = 1;
            newWeights.internetStipendWeight = 1;
            newWeights.accidentInsuranceWeight = 1;
            newWeights.tuitionReimbursementWeight = 1;
            sumOfWeights = 6;
        }

        double jobScore = (adjustedYearlySalary * newWeights.yearlySalaryWeight
                + adjustedYearlyBonus * newWeights.yearlyBonusWeight
                + matched401kValue * newWeights.match401kWeight
                + job.internetStipend * newWeights.internetStipendWeight
                + job.accidentInsurance * newWeights.accidentInsuranceWeight
                + job.tuitionReimbursement * newWeights.tuitionReimbursementWeight)
                / sumOfWeights;

        return jobScore;
    }

    public static RankedJob getRankedJob(Job job, Weights weights) {
        double adjustedYearlySalary = adjustForCostOfLiving(job.yearlySalary, job.costOfLivingIndex);
        double adjustedYearlyBonus = adjustForCostOfLiving(job.yearlyBonus, job.costOfLivingIndex);

        double jobScore = getJobScore(job, weights);

        return new RankedJob(job, adjustedYearlySalary, adjustedYearlyBonus, jobScore);
    }

    public static List<RankedJob> getSortedRankedJobs(List<Job> jobs, Weights weights) {
        //.toList() makes the string immutable, not able to sort!
        List<RankedJob> rankedJobs = jobs.stream()
                .map(job -> getRankedJob(job, weights))
                .collect(Collectors.toCollection(ArrayList::new));
        rankedJobs.sort(Comparator.comparingDouble((RankedJob r) -> r.jobScore).reversed());
        return rankedJobs;
    }
}
