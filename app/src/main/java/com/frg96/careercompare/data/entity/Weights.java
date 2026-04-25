package com.frg96.careercompare.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weights_table")
public class Weights {
    @PrimaryKey
    @ColumnInfo(name = "weights_id")
    public int weightsID = 1;

    @ColumnInfo(name = "salary_weight")
    public int yearlySalaryWeight = 1;

    @ColumnInfo(name = "bonus_weight")
    public int yearlyBonusWeight = 1;

    @ColumnInfo(name = "match401k_weight")
    public int match401kWeight = 1;

    @ColumnInfo(name = "internet_stipend_weight")
    public int internetStipendWeight = 1;

    @ColumnInfo(name = "accident_insurance_weight")
    public int accidentInsuranceWeight = 1;

    @ColumnInfo(name = "tuition_reimbursement_weight")
    public int tuitionReimbursementWeight = 1;

    public Weights() {
    }

    public Weights(Weights otherWeights) {

        if (otherWeights == null) {
            this.weightsID = 1;
            this.yearlySalaryWeight = 1;
            this.yearlyBonusWeight = 1;
            this.match401kWeight = 1;
            this.internetStipendWeight = 1;
            this.accidentInsuranceWeight = 1;
            this.tuitionReimbursementWeight = 1;
            return;
        }
        this.weightsID = otherWeights.weightsID;
        this.yearlySalaryWeight = otherWeights.yearlySalaryWeight;
        this.yearlyBonusWeight = otherWeights.yearlyBonusWeight;
        this.match401kWeight = otherWeights.match401kWeight;
        this.internetStipendWeight = otherWeights.internetStipendWeight;
        this.accidentInsuranceWeight = otherWeights.accidentInsuranceWeight;
        this.tuitionReimbursementWeight = otherWeights.tuitionReimbursementWeight;
    }

}
