package com.brainoptimax.peakstate.ui.activity.goals

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAddValueBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.adapter.valuegoals.ValueAdapter
import com.brainoptimax.peakstate.utils.Animatoo


class AddValueActivity : AppCompatActivity() {

    private var activityAddValueBinding: ActivityAddValueBinding? = null
    private val binding get() = activityAddValueBinding!!

    var valueAdapter: RecyclerView.Adapter<*>? = null

    @SuppressLint("InlinedApi")
    var valueName = arrayOf(
        "Community",
        "Education",
        "Finance",
        "Family",
        "Health",
        "Other",
    )

    // Define an integer array to hold the image recourse ids
    var valueImages = intArrayOf(
        R.drawable.ic_comunity_value,
        R.drawable.ic_education_value,
        R.drawable.ic_finance_value,
        R.drawable.ic_family_value,
        R.drawable.ic_medic_value,
        R.drawable.ic_other_value,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddValueBinding = ActivityAddValueBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtain a handle for the RecyclerView
        // You may use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        binding.rvValue.setHasFixedSize(true)
//        // Use a linear layout manager
//        val llm = LinearLayoutManager(this)
//        binding.rvValue.layoutManager = llm
//        // Create an instance of ProgramAdapter. Pass context and all the array elements to the constructor
//        valueAdapter = ValueAdapter(this, valueName, valueImages)
//        // Finally, attach the adapter with the RecyclerView
//        binding.rvValue.adapter = valueAdapter
//
//        binding.backMain.setOnClickListener {
//            startActivity(Intent(this, ValueGoalsActivity::class.java))
//            Animatoo.animateSwipeLeft(this)
//            finish()
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ValueGoalsActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}