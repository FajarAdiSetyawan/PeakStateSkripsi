package com.brainoptimax.peakmeup.viewmodel.reminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.brainoptimax.peakmeup.model.Reminders
import com.brainoptimax.peakmeup.repository.ReminderRepository

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