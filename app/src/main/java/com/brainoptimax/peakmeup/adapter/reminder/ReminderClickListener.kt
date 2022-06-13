package com.brainoptimax.peakmeup.adapter.reminder

import com.brainoptimax.peakmeup.model.Reminders

interface ReminderClickListener {

    fun click(reminders: Reminders?)
    fun OnLongClick(reminders: Reminders?)

}