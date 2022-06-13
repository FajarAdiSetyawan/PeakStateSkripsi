package com.brainoptimax.peakmeup.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.brainoptimax.peakmeup.database.AppDataBase.Companion.getInstance
import com.brainoptimax.peakmeup.database.ReminderDAO
import com.brainoptimax.peakmeup.model.Reminders

class ReminderRepository(application: Application?) {
    private var reminderDAO: ReminderDAO?

    @JvmField
    var getAllReminder: LiveData<List<Reminders?>?>?

    fun reminderInsert(reminders: Reminders?) {
        reminderDAO!!.insertReminders(reminders)
    }

    fun reminderDelete(reminders: Reminders?) {
        reminderDAO!!.deleteReminders(reminders)
    }

    fun reminderUpdate(reminders: Reminders?) {
        reminderDAO!!.updateReminder(reminders)
    }

    init {
        val appDataBase = getInstance(application!!)
        reminderDAO = appDataBase!!.reminderDao()
        getAllReminder = reminderDAO!!.allReminders
    }
}