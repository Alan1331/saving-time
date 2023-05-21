package com.example.savingtime.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavingPlanDao {

    @Insert
    fun insert(savingPlan: SavingPlanEntity)

    @Query("SELECT * FROM saving_plan ORDER BY goals_amount")
    fun getSavingPlan(): LiveData<List<SavingPlanEntity>>

    @Query("DELETE FROM saving_plan")
    fun clearData()

}