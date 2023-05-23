package com.example.savingtime.ui.savingPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingtime.db.SavingPlanEntity
import com.example.savingtime.db.SavingPlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavingPlanViewModel(private val savingPlanRepository: SavingPlanRepository) : ViewModel() {
    val data = savingPlanRepository.getSavingPlan()

    fun hapusData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savingPlanRepository.clearData()
        }
    }

    fun hapusSavingPlan(savingPlanEntity: SavingPlanEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            savingPlanRepository.deleteSavingPlan(savingPlanEntity)
        }
    }

    fun setorBulanan(id: Long, achievedAmount: Double, monthlyContribution: Double) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val newAmount = achievedAmount + monthlyContribution
            savingPlanRepository.setorBulanan(id, newAmount)
        }
    }

}