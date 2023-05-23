package com.example.savingtime.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavingPlanEntity::class], version = 1, exportSchema = false)
abstract class SavingPlanDb : RoomDatabase() {

    abstract val dao: SavingPlanDao

    companion object {

        @Volatile
        private var INSTANCE: SavingPlanDb? = null

        fun getInstance(context: Context): SavingPlanDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SavingPlanDb::class.java,
                        "saving_plan.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }


    }

}