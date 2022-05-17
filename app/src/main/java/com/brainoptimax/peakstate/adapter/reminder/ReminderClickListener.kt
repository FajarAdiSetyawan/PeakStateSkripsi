package com.brainoptimax.peakstate.adapter.reminder

import com.brainoptimax.peakstate.model.Reminders

interface ReminderClickListener {

    fun click(reminders: Reminders?)
    fun OnLongClick(reminders: Reminders?)

}