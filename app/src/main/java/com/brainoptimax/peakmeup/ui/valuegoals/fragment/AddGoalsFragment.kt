package com.brainoptimax.peakmeup.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakmeup.ui.valuegoals.fragment.bottomsheet.AddBottomSheetGoals
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentAddGoalsBinding
import com.brainoptimax.peakmeup.services.AlarmReceiverValueGoals
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.utils.ReminderUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

// mengambil data dari adapter value
private const val VALUE = "value"
private const val STATEMENT = "statement"

class AddGoalsFragment : Fragment() {

    private var fragmentAddGoalsBinding: FragmentAddGoalsBinding? = null
    private val binding get() = fragmentAddGoalsBinding!!

    private var value: String? = null
    private var statement: String? = null

    private lateinit var preference: Preferences

    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var navController: NavController

    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            value = it.getString(VALUE)
            statement = it.getString(STATEMENT)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGoalsFragment().apply {
                arguments = Bundle().apply {
                    putString(VALUE, param1)
                    putString(STATEMENT, param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddGoalsBinding = FragmentAddGoalsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        navController = Navigation.findNavController(requireView())

        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        createNotificationChannel()

        binding.tvSelectValue.text = value

        binding.cardDateGoals.setOnClickListener {
            setDate()
        }

        binding.cardTimeGoals.setOnClickListener {
            setTime()
        }

        binding.fab.setOnClickListener {

            val desc = binding.etDesc.text.toString().trim()
            val date = binding.tvDateGoals.text.toString().trim()
            val time = binding.tvTimeGoals.text.toString().trim()

            when {
                desc.isEmpty() -> {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.desc_goals_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                date.isEmpty() || date == resources.getString(R.string.date) -> {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.date_goals_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                time.isEmpty() || time == resources.getString(R.string.time) -> {
                    Toast.makeText(
                        activity, resources.getString(R.string.time_goals_empty), Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    viewModel.addGoal(uidUser!!, value!!, statement!!, date, time, desc)
                    viewModel.idGoalsMutableLiveData.observe(viewLifecycleOwner) { idGoals ->
                        if (idGoals!!.isNotEmpty()){
                            val args = Bundle()
                            args.putString("key", idGoals)
                            val newFragment: BottomSheetDialogFragment = AddBottomSheetGoals()
                            newFragment.arguments = args
                            newFragment.show(requireActivity().supportFragmentManager, "TAG")
                            setNotification(value!!, statement!!)
                        }

                    }

                    viewModel.databaseErrorAddGoals.observe(viewLifecycleOwner) { error ->
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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
        binding.tvDateGoals.text = formattedDate
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
        binding.tvTimeGoals.text = "$hour:$minute"
    }

    @SuppressLint("MissingPermission")
    private fun setNotification(value: String, statement: String) {
        val intent = Intent(requireActivity(), AlarmReceiverValueGoals::class.java)
        intent.action = "com.brainoptimax.peakstate.valuegoals"
        intent.putExtra("GoalsTitle", value)
        intent.putExtra("GoalsStatement", statement)
        Log.d("TAG", "onNotifyGoal: $value")

        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 101, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "goals_notify"
            val description = "To Notify Goals"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("goals_notify", name, importance)
            channel.description = description
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAddGoalsBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddGoalsBinding = null
    }
}

