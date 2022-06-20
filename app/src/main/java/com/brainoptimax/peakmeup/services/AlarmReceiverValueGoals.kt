package com.brainoptimax.peakmeup.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import java.util.*

class AlarmReceiverValueGoals : BroadcastReceiver() {
    var notificationId = (Date().time / 1000L % Int.MAX_VALUE).toInt()

    override fun onReceive(context: Context, intent: Intent) {
        when {
            // mengambil data yang dikirmkan dari add reminder
            intent.action.equals("com.brainoptimax.peakstate.valuegoals", ignoreCase = true) -> {
                val b = intent.extras
                val value = b!!.getString("GoalsTitle")
                val stat = b!!.getString("GoalsStatement")
                // pindah activity
                val intent1 = Intent(context, ValueGoalsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 1001, intent1, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                Log.d("TAG", "onNotifyGoals: $value")
                // buat notifikasi
                val builder = NotificationCompat.Builder(context, "goals_notify")
                    .setSmallIcon(R.drawable.peakstate_logo) //set icon for notification
                    .setContentTitle(value) //                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentText(stat)
                    .setAutoCancel(true) // makes auto cancel of notification
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT) //set priority of notification
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(notificationId, builder.build())
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