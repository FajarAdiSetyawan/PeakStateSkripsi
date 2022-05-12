package com.brainoptimax.peakstate.ui.activity.goals

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.GoalsAdapter
import com.brainoptimax.peakstate.databinding.ActivityAddGoalsBinding
import com.brainoptimax.peakstate.model.Goals
import com.brainoptimax.peakstate.model.ValueGoals
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddGoalsActivity : AppCompatActivity() {

    private var activityAddGoalsBinding: ActivityAddGoalsBinding? = null
    private val binding get() = activityAddGoalsBinding!!

    companion object {
        const val EXTRA_VALUE = "extra_value"
        const val EXTRA_STAT = "extra_stat"
    }

    private lateinit var goals: ArrayList<Goals>
    private lateinit var goalsAdapter: GoalsAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var datePickerDialog: DatePickerDialog? = null
    var dates: String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddGoalsBinding = ActivityAddGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val value = intent.getStringExtra(EXTRA_VALUE)
        val stat = intent.getStringExtra(EXTRA_STAT)

        // creating new array list.
        goals = ArrayList()
        // initializing our adapter class with our array list and context.
        goalsAdapter = GoalsAdapter(goals, this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvGoals.layoutManager = layoutManager
        binding.rvGoals.adapter = goalsAdapter

        binding.tvSelectValue.text = value

        binding.ivAdd.setOnClickListener {
            addGoals()
        }



        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedGoals: Goals = goals[viewHolder.adapterPosition]

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                goals.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                goalsAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                Snackbar.make(binding.rvGoals, deletedGoals.goals!!, Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        goals.add(position, deletedGoals)

                        goalsAdapter.notifyItemInserted(position)
                    }.show()
            } // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(binding.rvGoals)


        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        binding.cardDateGoals.setOnClickListener {
            datePickerDialog = DatePickerDialog(this,
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



        binding.btnSave.setOnClickListener {

            val desc = binding.etDesc.text.toString().trim()
            val date = binding.tvDateGoals.text.toString().trim()
            val time = binding.tvTimeGoals.text.toString().trim()

            if (goalsAdapter.itemCount == 0){
                Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
                    .show()
            }else{
                when {
                    desc.isEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
                            .show()
                    }
                    date.isEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
                            .show()
                    }
                    time.isEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        val dateTime = "$date $time"
                        val dateFormat = SimpleDateFormat("d-M-yyyy h:mm")
                        try {
                            val d = dateFormat.parse(dateTime)
                            val sdf =
                                SimpleDateFormat("E, d MMMM yyyy - h:mm", Locale.getDefault())
                            val dateTimes = sdf.format(d)
                            val reference = databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").push()
                            val id = reference.key
                            reference.setValue(ValueGoals(id, value, stat, date, time, dateTimes, desc, "null",goals)
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Value & Goals Recorded",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, ValueGoalsActivity::class.java))
                                Animatoo.animateSlideUp(this)
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Value & Goals Not Recorded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }
                }

            }

        }

        binding.backMain.setOnClickListener {
            super.onBackPressed()
            startActivity(Intent(this, AddValueActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, AddValueActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addGoals() {
        val goalsText = binding.etGoals.text.toString()
        if (goalsText.isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
                .show()
        } else {
            val goalsAdd = Goals(goalsText, "false")
            goals.add(goalsAdd)
            Toast.makeText(this, resources.getString(R.string.success_add_goals) + " " + goalsText, Toast.LENGTH_SHORT)
                .show()
            goalsAdapter.notifyDataSetChanged()
            binding.tvDelete.visibility = View.VISIBLE
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
            this,
            { view, hourOfDay, minute ->
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
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }
}