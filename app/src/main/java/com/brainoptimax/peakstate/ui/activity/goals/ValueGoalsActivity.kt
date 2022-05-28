package com.brainoptimax.peakstate.ui.activity.goals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakstate.databinding.ActivityValueGoalsBinding

class ValueGoalsActivity : AppCompatActivity() {

    private var activityValueGoalsBinding: ActivityValueGoalsBinding? = null
    private val binding get() = activityValueGoalsBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityValueGoalsBinding = ActivityValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}