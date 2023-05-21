package com.example.savingtime.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "saving_plan")
data class SavingPlanEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "description") @Nullable var description: String?,
    @ColumnInfo(name = "goals_amount") @Nullable var goalsAmount: Double?,
    @ColumnInfo(name = "achieved_amount") var achievedAmount: Double,
    @ColumnInfo(name = "monthly_contribution") var monthlyContribution: Double,
    @ColumnInfo(name = "interest") var interest: Double,
    @ColumnInfo(name = "passed_month") var passedMonth: Int,
    @ColumnInfo(name = "duration_in_month") var durationInMonth: Int,

)
