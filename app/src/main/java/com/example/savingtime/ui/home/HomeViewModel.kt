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
import com.example.savingtime.model.calculateSavingPlan
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
        val savingPlan = SavingPlanEntity(
            description = description,
            goalsAmount = goalsAmount.value,
            achievedAmount = 0.00,
            monthlyContribution = monthlyContribution,
            interest = interest,
            passedMonth = 0,
            durationInMonth = durationInMonth
        )

        goalsAmount.value = savingPlan.calculateSavingPlan(startingAmount, rounded)

        // if users save saving plan
        if (description != null) {

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    db.insert(savingPlan)
                }
            }
        }
    }

    fun getTotalAmount(): LiveData<Double?> = goalsAmount

}