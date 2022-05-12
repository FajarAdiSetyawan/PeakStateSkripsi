package com.brainoptimax.peakstate.ui.activity.reminders

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.database.ReminderDatabaseHandler
import com.brainoptimax.peakstate.databinding.ActivityAddRemindersBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.services.AlarmReceiverReminder
import com.brainoptimax.peakstate.services.ReminderService
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ReminderUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddRemindersActivity : AppCompatActivity() {

    // view binding
    private var activityAddRemindersBinding: ActivityAddRemindersBinding? = null
    private val binding get() = activityAddRemindersBinding!!

    private lateinit var alarmManager: AlarmManager
    private lateinit var databaseHandler: ReminderDatabaseHandler
    private val myCalendar = Calendar.getInstance()
    private var date: DatePickerDialog.OnDateSetListener? = null
    private var hour: Int = 0
    private var minute: Int = 0
    private var reminderSaved = Reminders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddRemindersBinding = ActivityAddRemindersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        databaseHandler = ReminderDatabaseHandler(this)

        if (intent.hasExtra("reminder")) {
            reminderSaved = intent.getSerializableExtra("reminder") as Reminders
        }


        date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        if (reminderSaved.id != 0L) {
            binding.tvTitleRemainder.text = reminderSaved.title
            binding.tvSubtitleRemainder.text = reminderSaved.subtitle
            binding.tvDateReminder.text = reminderSaved.date
            binding.tvTimeReminder.text = reminderSaved.time
            binding.tietNote.setText(reminderSaved.description)


            val split = reminderSaved.date.split("/")
            val date = split[0]
            val month = split[1]
            val year = split[2]

            val split1 = reminderSaved.time.split(":")
            val hour = split1[0]
            val minute = split1[1]

            myCalendar.set(Calendar.YEAR, year.toInt())
            myCalendar.set(Calendar.MONTH, month.toInt())
            myCalendar.set(Calendar.DAY_OF_MONTH, date.toInt())

            myCalendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
            myCalendar.set(Calendar.MINUTE, minute.toInt())
            myCalendar.set(Calendar.SECOND, 0)

            binding.btnSaveRemainder.text = getString(R.string.update)
            binding.btnDeleteRemainder.visibility = View.VISIBLE
        } else {
            updateDate()
            binding.btnSaveRemainder.text = getString(R.string.save)
            binding.btnDeleteRemainder.visibility = View.INVISIBLE
        }

        binding.cardDateReminder.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
            datePickerDialog.show()
        }

        binding.cardTimeReminder.setOnClickListener {
            hour = myCalendar.get(Calendar.HOUR_OF_DAY)
            minute = myCalendar.get(Calendar.MINUTE)

            val timePickerDialog =
                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        myCalendar.set(Calendar.MINUTE, minute)
                        myCalendar.set(Calendar.SECOND, 0)
                        updateTime(hour, minute)
                    }, hour, minute, true
                )

            timePickerDialog.show()
        }

        val item1 = arrayOf(
            resources.getString(R.string.morning_routine),
            resources.getString(R.string.night_routine),
            resources.getString(R.string.movement),
            resources.getString(R.string.fresh_air)
        )
        binding.cardTitle.setOnClickListener {
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
            ) { dialog, which ->
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


                val reminder = Reminders()

                reminder.title = getTitle
                reminder.subtitle = getSubtitle
                reminder.description = getNote
                reminder.time = getTime
                reminder.date = getDate

                val saveReminderId: Long =
                    if (reminderSaved.id != 0L) {
                        reminder.id = reminderSaved.id
                        databaseHandler.updateReminder(reminder)
                        reminderSaved.id
                    } else {
                        databaseHandler.saveReminder(reminder)
                    }
                if (saveReminderId != 0L) {
                    Log.d("AlarmTime", "Hour: $hour")
                    Log.d("AlarmTime", "Min: $minute")
                    setRemainderAlarm(saveReminderId)
                } else {
                    ReminderUtils.showToastMessage(this, "Failed to save remainder")
                }
            }
        }

        binding.btnDeleteRemainder.setOnClickListener {
            val reminder = Reminders()

            if (reminderSaved.id != 0L){
                reminder.id = reminderSaved.id
                MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                    .setTitle("DELETE REMINDER")
                    .setMessage("Are you sure want to Delete ${reminderSaved.title} ?")
                    .setPositiveButton("Ok") { _, _ ->
                        databaseHandler.deleteReminderById(reminder.id)
                        startActivity(Intent(this, ListRemindersActivity::class.java))
                        Animatoo.animateSwipeLeft(this)
                        finish()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { _, _ -> }
                    .show()
            }
        }

        binding.btnBackMenu.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }

    }

    private fun updateDate() {
        val formattedDate =
            ReminderUtils.getFormattedDateInString(myCalendar.timeInMillis, "dd/MM/YYYY")
        binding.tvDateReminder.text = formattedDate
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        binding.tvTimeReminder.text = "$hour:$minute"
    }

    @SuppressLint("ObsoleteSdkInt", "UnspecifiedImmutableFlag")
    private fun setRemainderAlarm(savedReminderId: Long) {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderService = ReminderService()
        val reminderReceiverIntent = Intent(this, AlarmReceiverReminder::class.java)

        reminderReceiverIntent.putExtra("reminderId", savedReminderId)
        reminderReceiverIntent.putExtra("isServiceRunning", isServiceRunning(reminderService))
        val pendingIntent =
            PendingIntent.getBroadcast(this, savedReminderId.toInt(), reminderReceiverIntent, 0)
        val formattedDate =
            ReminderUtils.getFormattedDateInString(myCalendar.timeInMillis, "dd/MM/YYYY HH:mm")
        Log.d("TimeSetInMillis:", formattedDate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent
            )
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent)
        }

        ReminderUtils.showToastMessage(this, "Alarm is set at : $formattedDate")
        finish()
    }

    @Suppress("DEPRECATION")
    private fun isServiceRunning(reminderService: ReminderService): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (reminderService.javaClass.name == service.service.className) {
                Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()

    }
}