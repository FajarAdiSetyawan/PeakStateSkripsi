package com.brainoptimax.peakstate.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.brainoptimax.peakstate.database.AppDataBase.Companion.getInstance
import com.brainoptimax.peakstate.database.ReminderDAO
import com.brainoptimax.peakstate.model.Reminders

class ReminderRepository(application: Application?) {
    var reminderDAO: ReminderDAO?

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