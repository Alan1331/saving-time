package com.example.savingtime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingtime.db.SavingPlanEntity
import com.example.savingtime.db.SavingPlanRepository
import com.example.savingtime.model.calculateSavingPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val savingPlanRepository: SavingPlanRepository) : ViewModel() {

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
                    savingPlanRepository.insert(savingPlan)
                }
            }
        }
    }

    fun getTotalAmount(): LiveData<Double?> = goalsAmount

}