package com.brainoptimax.peakstate.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.ui.reminders.ListRemindersActivity


class AlarmReceiverReminder : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when {
            // mengambil data yang dikirmkan dari add reminder
            intent.action.equals("com.brainoptimax.peakstate.reminder", ignoreCase = true) -> {
                val b = intent.extras

                // pindah activity
                val intent1 = Intent(context, ListRemindersActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 101, intent1, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

                // buat notifikasi
                val builder = NotificationCompat.Builder(context, "reminder_notify")
                    .setSmallIcon(R.drawable.peakstate_logo) //set icon for notification
                    .setContentTitle(b!!.getString("ReminderTitle")) //                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(
                        NotificationCompat.InboxStyle()
                            .addLine(b.getString("ReminderDesc"))
                            .setBigContentTitle(b.getString("ReminderSubTitle"))
                    )
                    .setAutoCancel(true) // makes auto cancel of notification
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) //set priority of notification
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(123, builder.build())
            }
            intent.action.equals(
                "android.intent.action.BOOT_COMPLETED",
                ignoreCase = true
            ) -> {
                // phone restart
            }
        }





    }
}