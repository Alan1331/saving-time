package com.example.savingtime.db

import androidx.lifecycle.LiveData

class SavingPlanRepository(private val savingPlanDao: SavingPlanDao) {

    fun insert(savingPlan: SavingPlanEntity) = savingPlanDao.insert(savingPlan)

    fun getSavingPlan(): LiveData<List<SavingPlanEntity>> = savingPlanDao.getSavingPlan()

    fun clearData() = savingPlanDao.clearData()

    fun deleteSavingPlan(savingPlan: SavingPlanEntity) = savingPlanDao.deleteSavingPlan(savingPlan)

    fun setorBulanan(id: Long, newAmount: Double) = savingPlanDao.setorBulanan(id, newAmount)
}