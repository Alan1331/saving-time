package com.example.savingtime.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SavingPlanDao {

    @Insert
    fun insert(savingPlan: SavingPlanEntity)

    @Query("SELECT * FROM saving_plan ORDER BY goals_amount")
    fun getSavingPlan(): LiveData<List<SavingPlanEntity>>

    @Query("DELETE FROM saving_plan")
    fun clearData()

    @Delete
    fun deleteSavingPlan(savingPlan: SavingPlanEntity)

    @Query("UPDATE saving_plan SET achieved_amount = (:newAmount) WHERE id = :id")
    fun setorBulanan(id: Long, newAmount: Double)

}