package com.brainoptimax.peakstate.ui.activity.reminders

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAddRemindersBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.services.AlarmReceiverReminder
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ReminderUtils
import com.brainoptimax.peakstate.viewmodel.reminder.ReminderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class AddRemindersActivity : AppCompatActivity() {

    // view binding
    private var activityAddRemindersBinding: ActivityAddRemindersBinding? = null
    private val binding get() = activityAddRemindersBinding!!

    var viewModel: ReminderViewModel? = null
    var oldReminders = true
    private var reminders: Reminders? = null
    var status = false
    private var calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddRemindersBinding = ActivityAddRemindersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createNotificationChannel()

        viewModel = ViewModelProviders.of(this)[ReminderViewModel::class.java]
        try {
            updateReminders()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.cardTitle.setOnClickListener {
            val item1 = arrayOf(
                resources.getString(R.string.morning_routine),
                resources.getString(R.string.night_routine),
                resources.getString(R.string.movement),
                resources.getString(R.string.fresh_air)
            )

            val builder = AlertDialog.Builder(this, R.style.MaterialAlertDialogRounded)
            builder.setTitle("Title Remainder")
            // set the custom layout
            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_reminder, null)
            builder.setView(customLayout)

            builder.setSingleChoiceItems(
                item1, -1
            ) { dialog, which ->
                binding.tvTitleRemainder.text = item1[which]
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                editText.setText(item1[which])
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val editText: TextInputEditText = customLayout.findViewById(R.id.it_title_remainder)
                val data = editText.text.toString()
                if (data.isEmpty()) {
                    binding.tvTitleRemainder.text =
                        resources.getString(R.string.title_reminder)
                } else {
                    binding.tvTitleRemainder.text = data
                }
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.md_blue_400)
            )


        }

        binding.cardSubtitle.setOnClickListener {
            val item2 = arrayOf(
                resources.getString(R.string.write_journal),
                resources.getString(R.string.anchoring),
                resources.getString(R.string.visualization),
                resources.getString(R.string.breathing_exercise),
                resources.getString(R.string.brainwave_entrainment),
                resources.getString(R.string.values_and_goals),
            )

            val builder = AlertDialog.Builder(this, R.style.MaterialAlertDialogRounded)
            builder.setTitle("Sub Title Remainder")
            // set the custom layout
            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_reminder, null)
            builder.setView(customLayout)

            builder.setSingleChoiceItems(
                item2, -1
            ) { _, which ->
                binding.tvSubtitleRemainder.text = item2[which]
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                //                        String data = editText.getText().toString();
                //                        tvTitle.setText(data);
                editText.setText(item2[which])
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                val data = editText.text.toString()
                if (data.isEmpty()) {
                    binding.tvSubtitleRemainder.text =
                        resources.getString(R.string.sub_title_reminder)
                } else {
                    binding.tvSubtitleRemainder.text = data
                }
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.md_blue_400)
            )

        }

        binding.cardTimeReminder.setOnClickListener {
            setTime()
        }

        binding.cardDateReminder.setOnClickListener {
            setDate()
        }


        binding.btnSaveRemainder.setOnClickListener {
            val getTime: String = binding.tvTimeReminder.text.toString().trim()
            val getDate: String = binding.tvDateReminder.text.toString().trim()
            val getTitle: String =
                binding.tvTitleRemainder.text.toString().trim()
            val getSubtitle: String =
                binding.tvSubtitleRemainder.text.toString().trim()
            val getNote: String = binding.tietNote.text.toString().trim()

            if (getTitle.isEmpty() || getTitle == resources.getString(R.string.title_reminder)) {
                Toast.makeText(applicationContext, "Title Not Blank", Toast.LENGTH_SHORT).show()
            } else if (getSubtitle.isEmpty() || getSubtitle == resources.getString(R.string.sub_title_reminder)) {
                Toast.makeText(applicationContext, "Sub Title Not Blank", Toast.LENGTH_SHORT).show()
            } else if (getDate.isEmpty()) {
                Toast.makeText(applicationContext, "Date Not Blank", Toast.LENGTH_SHORT).show()
            } else if (getTime.isEmpty() || getTime == resources.getString(R.string.time)) {
                Toast.makeText(applicationContext, "Time Not Blank", Toast.LENGTH_SHORT).show()
            } else if (getNote.isEmpty()) {
                Toast.makeText(applicationContext, "Note Not Blank", Toast.LENGTH_SHORT).show()
                binding.tilNote.error =
                    resources.getString(R.string.not_verified)
                binding.tilNote.requestFocus()
            } else {
                binding.tilNote.error = null

                MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                    .setTitle("Confirm the action")
                    .setMessage("Are you sure you add Reminder ?")
                    .setPositiveButton("Ok") { _, _ ->
                        setReminders(getTitle, getSubtitle, getNote, getDate, getTime)
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> }
                    .show()
            }
        }

        binding.btnBackMenu.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }

    private fun setReminders(title: String, subTitle: String, description: String, dates: String, time: String) {
        @SuppressLint("SimpleDateFormat") val fmt = SimpleDateFormat("EEE,yyyy-MM-dd HH:mm a")
        val date = Date()
        if (oldReminders) {
            reminders = Reminders()
            reminders!!.title = title
            reminders!!.subtitle = subTitle
            reminders!!.description = description
            reminders!!.date = dates
            reminders!!.time = time
            reminders!!.datetime = fmt.format(date)
            viewModel!!.insertReminders(reminders)
            setNotification(title, subTitle, description)
            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
        } else {
            reminders!!.title = title
            reminders!!.subtitle = subTitle
            reminders!!.description = description
            reminders!!.date = dates
            reminders!!.time = time
            reminders!!.datetime = fmt.format(date)
            viewModel!!.updateReminders(reminders)
            setNotification(title, subTitle, description)
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun updateReminders() {
        val intent = intent
        reminders = intent.getSerializableExtra("updateReminders") as Reminders?
        binding.tvTitleRemainder.text = reminders!!.title
        binding.tvSubtitleRemainder.text = reminders!!.subtitle
        binding.tvDateReminder.text = reminders!!.date
        binding.tvTimeReminder.text = reminders!!.time
        binding.tietNote.setText(reminders!!.description)
        oldReminders = false

        binding.btnSaveRemainder.text = getString(R.string.update)
    }

    private fun setDate() {
        val Year = calendar[Calendar.YEAR]
        val Month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        val datePickerDialog = DatePickerDialog(this, { view, YEAR, MONTH, DATE ->
            calendar[Calendar.YEAR] = YEAR
            calendar[Calendar.MONTH] = MONTH
            calendar[Calendar.DATE] = DATE
        }, Year, Month, date)
        datePickerDialog.show()
        updateDate()
    }

    private fun updateDate() {
        val formattedDate =
            ReminderUtils.getFormattedDateInString(calendar.timeInMillis, "dd/MM/YYYY")
        binding.tvDateReminder.text = formattedDate
    }

    private fun setTime() {
        val Hour = calendar[Calendar.HOUR_OF_DAY]
        val Minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this, { view, hour, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            updateTime(hour, minute)
        }, Hour, Minute, false)
        timePickerDialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        binding.tvTimeReminder.text = "$hour:$minute"
    }

    @SuppressLint("MissingPermission")
    private fun setNotification(title: String, subTitle: String, desc: String) {
        val intent = Intent(this, AlarmReceiverReminder::class.java)
        intent.action = "com.brainoptimax.peakstate.reminder"
        intent.putExtra("ReminderTitle", title)
        intent.putExtra("ReminderSubTitle", subTitle)
        intent.putExtra("ReminderDesc", desc)

        val pendingIntent = PendingIntent.getBroadcast(this, 101, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "reminder_notify"
            val description = "To Notify Reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_notify", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}