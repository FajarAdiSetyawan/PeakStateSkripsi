package com.brainoptimax.peakstate.ui.activity.goals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.EditGoalsAdapter
import com.brainoptimax.peakstate.databinding.ActivityEditValueGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class EditValueGoalsActivity : AppCompatActivity() {

    private var activityEditValueGoalsBinding: ActivityEditValueGoalsBinding? = null
    private val binding get() = activityEditValueGoalsBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var goals: ArrayList<ToDo>
    private lateinit var editGoalsAdapter: EditGoalsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityEditValueGoalsBinding = ActivityEditValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val valueID = intent.getStringExtra("valueId")

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // creating new array list.
        goals = ArrayList()
        // initializing our adapter class with our array list and context.
//        editGoalsAdapter = EditGoalsAdapter(goals, valueID)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvGoals.layoutManager = layoutManager
        binding.rvGoals.adapter = editGoalsAdapter




        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
            .child(valueID!!)
            .addValueEventListener(object : ValueEventListener {


                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val value = dataSnapshot.child("value").value.toString()
                        val statement = dataSnapshot.child("statement").value.toString()
                        val descValue = dataSnapshot.child("descValue").value.toString()
                        val date = dataSnapshot.child("date").value.toString()
                        val time = dataSnapshot.child("time").value.toString()

                        binding.etValue.setText(value)
                        binding.etSteatment.setText(statement)
                        binding.etDesc.setText(descValue)
                        binding.tvDateGoals.text = date
                        binding.tvTimeGoals.text = time
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EditValueGoalsActivity,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
            .child(valueID).child("goals")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        goals = arrayListOf()

                        for (item in dataSnapshot.children) {
                            val toDoList = item.getValue(ToDo::class.java)
                            this@EditValueGoalsActivity.goals.add(toDoList!!)
                        }
//                        binding.rvGoals.adapter = EditGoalsAdapter(goals, valueID)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EditValueGoalsActivity,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        binding.tvValueid.text = valueID

        binding.backMain.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        binding.ivAdd.setOnClickListener {
//            addGoals()
        }

        binding.btnSave.setOnClickListener {

            val desc = binding.etDesc.text.toString().trim()
            val value = binding.etValue.text.toString().trim()
            val stat = binding.etSteatment.text.toString().trim()
            val date = binding.tvDateGoals.text.toString().trim()
            val time = binding.tvTimeGoals.text.toString().trim()

            if (goals.size == 0){
                Toast.makeText(this, "snskn", Toast.LENGTH_SHORT)
                    .show()
            }else{
                when {
                    value.isEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.value_empty), Toast.LENGTH_SHORT)
                            .show()
                    }
                    stat.isEmpty() -> {
                        Toast.makeText(this, resources.getString(R.string.stat_empty), Toast.LENGTH_SHORT)
                            .show()
                    }
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

                }

            }

        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun addGoals() {
//        val goalsText = binding.etGoals.text.toString()
//        val valueID = binding.tvValueid.text.toString()
//
//        val position = goals.size
//        if (goalsText.isEmpty()) {
//            Toast.makeText(this, resources.getString(R.string.goals_empty), Toast.LENGTH_SHORT)
//                .show()
//        } else {
//            val goalsAdd = Goals(goalsText, "false")
//            databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
//                .child(valueID).child("goals").child(position.toString()).setValue(goalsAdd).addOnSuccessListener {
//                    Toast.makeText(
//                        this, resources.getString(R.string.success_add_goals) + " " + goalsText,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }.addOnFailureListener {
//                    Toast.makeText(
//                        this,
//                        "Error Add",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            editGoalsAdapter.notifyDataSetChanged()
//        }
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}