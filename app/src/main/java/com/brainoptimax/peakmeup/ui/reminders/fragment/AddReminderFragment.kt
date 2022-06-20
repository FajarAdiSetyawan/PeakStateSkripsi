package com.brainoptimax.peakmeup.ui.reminders.fragment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentAddReminderBinding
import com.brainoptimax.peakmeup.model.Reminder
import com.brainoptimax.peakmeup.services.AlarmReceiverReminder
import com.brainoptimax.peakmeup.ui.reminders.ReminderActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.utils.ReminderUtils
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddReminderFragment : Fragment() {
    private var fragmentAddReminderBinding: FragmentAddReminderBinding? = null
    private val binding get() = fragmentAddReminderBinding!!

    private lateinit var preference: Preferences
    private lateinit var viewModel: ReminderViewModel
    private lateinit var nav : NavController
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddReminderBinding = FragmentAddReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = Navigation.findNavController(requireView())

        viewModel = ViewModelProviders.of(this)[ReminderViewModel::class.java]

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        createNotificationChannel()

        binding.cardTitle.setOnClickListener {
            val item1 = arrayOf(
                resources.getString(R.string.morning_routine),
                resources.getString(R.string.night_routine),
                resources.getString(R.string.movement),
                resources.getString(R.string.fresh_air)
            )

            val builder = AlertDialog.Builder(requireActivity(), R.style.MaterialAlertDialogRounded)
            builder.setTitle(resources.getString(R.string.title_reminder))
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
                resources.getString(R.string.cancel)
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

            val builder = AlertDialog.Builder(requireActivity(), R.style.MaterialAlertDialogRounded)
            builder.setTitle(resources.getString(R.string.sub_title_reminder))
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
                resources.getString(R.string.cancel)
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
            val getNote: String = binding.etNote.text.toString().trim()

            if (getTitle.isEmpty() || getTitle == resources.getString(R.string.title_reminder)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.title_blank), Toast.LENGTH_SHORT).show()
            } else if (getSubtitle.isEmpty() || getSubtitle == resources.getString(R.string.sub_title_reminder)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.sub_title_blank), Toast.LENGTH_SHORT).show()
            } else if (getDate.isEmpty()) {
                Toast.makeText(requireActivity(), resources.getString(R.string.date_blank), Toast.LENGTH_SHORT).show()
            } else if (getTime.isEmpty() || getTime == resources.getString(R.string.time)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.blank), Toast.LENGTH_SHORT).show()
            } else if (getNote.isEmpty()) {
                Toast.makeText(requireActivity(), resources.getString(R.string.note_blank), Toast.LENGTH_SHORT).show()
                binding.etNote.error = resources.getString(R.string.note_blank)
                binding.etNote.requestFocus()
            } else {
                binding.etNote.error = null

                MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                    .setTitle(resources.getString(R.string.confirm_action))
                    .setMessage(resources.getString(R.string.dialog_reminder_add_msg))
                    .setPositiveButton("Ok") { _, _ ->
                        viewModel.openLoadingSaveDialog(requireActivity())
                        viewModel.addReminder(uidUser!!, getTitle, getSubtitle, getNote, getDate, getTime)
                        viewModel.addReminderMutableLiveData.observe(viewLifecycleOwner){ status ->
                            if (status.equals("success")){
                                setNotification(getTitle, getSubtitle, getNote)
                                startActivity(Intent(context, ReminderActivity::class.java)) // pindah ke login
                                Animatoo.animateSlideUp(requireContext())
                                viewModel.closeLoadingDialog()
                            }
                        }

                        viewModel.databaseErrorAddReminder.observe(viewLifecycleOwner
                        ) { error ->
                            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton(
                        resources.getString(R.string.cancel)
                    ) { dialog, which -> }
                    .show()
            }
        }

        binding.btnBackMenu.setOnClickListener {
            nav.navigate(R.id.action_addReminderFragment_to_listReminderFragment)
        }

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, ReminderActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun setDate() {
        val Year = calendar[Calendar.YEAR]
        val Month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        val datePickerDialog = DatePickerDialog(requireActivity(), { view, YEAR, MONTH, DATE ->
            calendar[Calendar.YEAR] = YEAR
            calendar[Calendar.MONTH] = MONTH
            calendar[Calendar.DATE] = DATE
        }, Year, Month, date)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
        updateDate()
    }

    private fun updateDate() {
        val formattedDate =
            ReminderUtils.getFormattedDateInString(calendar.timeInMillis, "dd-MMM-yyyy")
        binding.tvDateReminder.text = formattedDate
    }

    private fun setTime() {
        val Hour = calendar[Calendar.HOUR_OF_DAY]
        val Minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(requireActivity(), { view, hour, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            updateTime(hour, minute)
        }, Hour, Minute, true)
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        binding.tvTimeReminder.text = "$hour:$minute"
    }

    @SuppressLint("MissingPermission")
    private fun setNotification(title: String, subTitle: String, desc: String) {
        val intent = Intent(requireActivity(), AlarmReceiverReminder::class.java)
        intent.action = "com.brainoptimax.peakstate.reminders"
        intent.putExtra("ReminderTitle", title)
        intent.putExtra("ReminderSubTitle", subTitle)
        intent.putExtra("ReminderDesc", desc)

        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 101, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "reminder_notify"
            val description = "To Notify Reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_notify", name, importance)
            channel.description = description
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }


}