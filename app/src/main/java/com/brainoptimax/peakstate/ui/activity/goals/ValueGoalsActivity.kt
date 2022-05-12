package com.brainoptimax.peakstate.ui.activity.goals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.adapter.valuegoals.ValueGoalsAdapter
import com.brainoptimax.peakstate.databinding.ActivityValueGoalsBinding
import com.brainoptimax.peakstate.model.ValueGoals
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class ValueGoalsActivity : AppCompatActivity() {

    private var activityValueGoalsBinding: ActivityValueGoalsBinding? = null
    private val binding get() = activityValueGoalsBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var valueGoals: ArrayList<ValueGoals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityValueGoalsBinding = ActivityValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvValue.layoutManager = layoutManager


        showLoading()
        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    when {
                        dataSnapshot.exists() -> {
                            goneLoading()
                            binding.layoutEmptyValue.visibility = View.INVISIBLE

                            valueGoals = arrayListOf()

                            for (item in dataSnapshot.children) {
                                val valueGoalsList = item.getValue(ValueGoals::class.java)
                                this@ValueGoalsActivity.valueGoals.add(valueGoalsList!!)
                            }
                            binding.rvValue.adapter = ValueGoalsAdapter(valueGoals)
                        }
                        else -> {
                            binding.layoutEmptyValue.visibility = View.VISIBLE
                            binding.rvValue.visibility = View.INVISIBLE
                            binding.shimmerValueGoals.stopShimmer()
                            binding.shimmerValueGoals.visibility = View.INVISIBLE
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ValueGoalsActivity,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        binding.fabAddValue.setOnClickListener {
            startActivity(Intent(this, AddValueActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        binding.backMain.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }
    }

    private fun showLoading(){
        binding.shimmerValueGoals.startShimmer()
        binding.shimmerValueGoals.visibility = View.VISIBLE
        binding.rvValue.visibility = View.INVISIBLE
    }

    private fun goneLoading(){
        binding.shimmerValueGoals.stopShimmer()
        binding.shimmerValueGoals.visibility = View.INVISIBLE
        binding.rvValue.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}