package com.example.savingtime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.savingtime.db.SavingPlanRepository

class HomeViewModelFactory(
    private val savingPlanRepository: SavingPlanRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(savingPlanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}