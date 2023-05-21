package com.example.savingtime.ui.home

import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingtime.R
import com.example.savingtime.db.SavingPlanDao
import com.example.savingtime.db.SavingPlanEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val db: SavingPlanDao) : ViewModel() {

    private val goalsAmount = MutableLiveData<Double?>()

    fun calculate(
        startingAmount: Double,
        monthlyContribution: Double,
        durationInMonth: Int,
        interest: Double,
        rounded: Boolean,
        description: String?
    ) {
        var totalAmount = startingAmount

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

        goalsAmount.value = totalAmount

        // if users save saving plan
        if (description != null) {

            val savingPlan = SavingPlanEntity(
                description = description,
                goalsAmount = totalAmount,
                achievedAmount = 0.00,
                monthlyContribution = monthlyContribution,
                interest = interest,
                passedMonth = 0,
                durationInMonth = durationInMonth
            )

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    db.insert(savingPlan)
                }
            }
        }
    }

    fun getTotalAmount(): LiveData<Double?> = goalsAmount

}