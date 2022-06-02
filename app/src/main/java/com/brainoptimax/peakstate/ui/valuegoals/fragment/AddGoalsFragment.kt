package com.brainoptimax.peakstate.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAddGoalsBinding
import com.brainoptimax.peakstate.ui.valuegoals.fragment.bottomsheet.AddBottomSheetGoals
import com.brainoptimax.peakstate.viewmodel.valuegoals.ValueGoalsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

// mengambil data dari adapter value
private const val VALUE = "value"
private const val STATEMENT = "statement"

class AddGoalsFragment : Fragment() {

    private var fragmentAddGoalsBinding: FragmentAddGoalsBinding? = null
    private val binding get() = fragmentAddGoalsBinding!!

    private var value: String? = null
    private var statement: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var datePickerDialog: DatePickerDialog? = null
    private var dates: String? = null

    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var navController: NavController

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

        navController = Navigation.findNavController(requireView())

        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid)
                .child("ValueGoals")

        val dbPush = databaseReference.push()
        val id = dbPush.key

        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        binding.tvSelectValue.text = value

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        binding.cardDateGoals.setOnClickListener {
            datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, year, month, dayOfMonth ->
                    dates = getDateString(dayOfMonth, month, year)
                    val date = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                    binding.tvDateGoals.text = date
                }, year, month, date
            )
            datePickerDialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog!!.show()
        }

        binding.cardTimeGoals.setOnClickListener {
            showTimeDialog()
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
                    viewModel.saveGoal(
                        dbPush,
                        view,
                        id!!,
                        value!!,
                        statement!!,
                        date,
                        time,
                        desc,
                        "",
                    )
                    viewModel.status.observe(viewLifecycleOwner) { status ->
                        status?.let {
                            //Reset status value at first to prevent multitriggering
                            //and to be available to trigger action again
                            viewModel.status.value = null

                            val args = Bundle()
                            args.putString("key", id)
                            val newFragment: BottomSheetDialogFragment = AddBottomSheetGoals()
                            newFragment.arguments = args
                            newFragment.show(requireActivity().supportFragmentManager, "TAG")
                        }
                    }
                }
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateString(year: Int, mMonth: Int, mDay: Int): String? {
        val calendar = Calendar.getInstance()
        calendar[year, mMonth] = mDay
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun showTimeDialog() {
        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        val calendar = Calendar.getInstance()

        /**
         * Initialize TimePicker Dialog
         */
        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { view, hourOfDay, minute ->
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */

                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                binding.tvTimeGoals.text = "$hourOfDay:$minute"
            },
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            DateFormat.is24HourFormat(requireActivity())
        )
        timePickerDialog.show()
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

