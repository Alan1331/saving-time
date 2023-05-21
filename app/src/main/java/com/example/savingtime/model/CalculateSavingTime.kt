package com.example.savingtime.model

import com.example.savingtime.db.SavingPlanEntity

fun SavingPlanEntity.calculateSavingPlan(startingAmount: Double, rounded: Boolean): Double {
    var totalAmount: Double = startingAmount

    // Calculation if interest is not applied
    if (interest == 0.00) {
        for (i in 1..durationInMonth) {
            totalAmount += monthlyContribution
        }
    } else { // Calculation if interest is applied
        val monthlyInterestRate = interest / 12 / 100
        for (i in 1..durationInMonth) {
            totalAmount += monthlyContribution;
            totalAmount *= (1 + monthlyInterestRate)
        }
    }

    // Round the total to the nearest thousand
    if (rounded) {
        totalAmount /= 1000
        totalAmount = kotlin.math.round(totalAmount)
        totalAmount *= 1000
    }

    return totalAmount
}