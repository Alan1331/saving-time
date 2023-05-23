package com.example.savingtime.ui.savingPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savingtime.db.SavingPlanDao
import com.example.savingtime.db.SavingPlanRepository

class SavingPlanViewModelFactory(
    private val savingPlanRepository: SavingPlanRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavingPlanViewModel::class.java)) {
            return SavingPlanViewModel(savingPlanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}