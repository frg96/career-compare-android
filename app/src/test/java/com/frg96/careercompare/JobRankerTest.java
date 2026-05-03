package com.frg96.careercompare;

import static org.junit.Assert.assertEquals;

import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.domain.util.JobRanker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JobRankerTest {
    Job job = new Job();
    Weights weights = new Weights();

    @Before
    public void setUp(){
        //setting easily calculated values for verification
        job.title = "Software engineer";
        job.company = "Global";
        job.city = "Baton Rouge";
        job.state = "Louisiana";
        job.costOfLivingIndex = 110;
        job.yearlySalary = 100000.;
        job.yearlyBonus = 1000.;
        job.match401k = 1;
        job.internetStipend = 100.;
        job.accidentInsurance = 10000.;
        job.tuitionReimbursement = 1000.;

        weights.yearlySalaryWeight = 2;
        weights.yearlyBonusWeight = 3;
        weights.match401kWeight = 4;
        weights.internetStipendWeight = 5;
        weights.accidentInsuranceWeight = 6;
        weights.tuitionReimbursementWeight = 7;
    }

    @Test
    public void adjustForCostOfLiving_validInputs_returnsCorrectValueSalary(){
        job.yearlySalary = 120000.;
        job.costOfLivingIndex = 90;
        double adjusted_salary = JobRanker.adjustForCostOfLiving(job.yearlySalary, job.costOfLivingIndex);
        assertEquals(133333.33, adjusted_salary, .01);
    }

    @Test
    public void adjustForCostOfLiving_validInputs_returnsCorrectValueBonus(){
        job.yearlyBonus = 10000.;
        job.costOfLivingIndex = 175;
        double adjusted_bonus = JobRanker.adjustForCostOfLiving(job.yearlyBonus, job.costOfLivingIndex);
        assertEquals(5714.29, adjusted_bonus, .01);
    }

    @Test
    public void getJobScore_validInputs_returnsCorrectScore(){
        double score = JobRanker.getJobScore(job, weights);
        assertEquals(9469.696967, score,.01);
    }

    @Test
    public void getJobScore_zeroWeights_weightsAreOne(){
        weights.yearlySalaryWeight = 0;
        weights.yearlyBonusWeight = 0;
        weights.match401kWeight = 0;
        weights.internetStipendWeight = 0;
        weights.accidentInsuranceWeight = 0;
        weights.tuitionReimbursementWeight = 0;
        double score = JobRanker.getJobScore(job, weights);

        assertEquals(17304.5454, score,.01);

        //calculated by hand the score when weights are all 1
    }
}
