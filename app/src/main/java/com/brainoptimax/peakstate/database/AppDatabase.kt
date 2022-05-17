package com.brainoptimax.peakstate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brainoptimax.peakstate.model.Reminders

@Database(entities = [Reminders::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun reminderDao() : ReminderDAO?

    companion object {
        var appDataBase: AppDataBase? = null
        var AppDatabase_Name = "PSDB"

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDataBase? {
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    AppDatabase_Name
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return appDataBase
        }
    }

}