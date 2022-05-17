package com.brainoptimax.peakstate.viewmodel.reminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.repository.ReminderRepository

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    var repository: ReminderRepository

    @JvmField
    var getAllReminders: LiveData<List<Reminders?>?>?

    fun insertReminders(reminders: Reminders?) {
        repository.reminderInsert(reminders)
    }

    fun deleteReminders(reminders: Reminders?) {
        repository.reminderDelete(reminders)
    }

    fun updateReminders(reminders: Reminders?) {
        repository.reminderUpdate(reminders)
    }


    init {
        repository = ReminderRepository(application)
        getAllReminders = repository.getAllReminder
    }
}