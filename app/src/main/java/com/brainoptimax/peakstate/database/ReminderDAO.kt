package com.brainoptimax.peakstate.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.brainoptimax.peakstate.model.Reminders

@Dao
interface ReminderDAO {

    @get:Query("SELECT * FROM reminder_table ORDER BY ID DESC")
    val allReminders: LiveData<List<Reminders?>?>?

    @Insert
    fun insertReminders(reminders: Reminders?)

    @Delete
    fun deleteReminders(reminders: Reminders?)

    @Update
    fun updateReminder(reminders: Reminders?)

}