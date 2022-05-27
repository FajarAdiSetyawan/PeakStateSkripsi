package com.brainoptimax.peakstate.ui.activity.goals

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakstate.databinding.ActivityAddGoalsBinding
import com.brainoptimax.peakstate.utils.Animatoo

class AddGoalsActivity : AppCompatActivity() {

    private var activityAddGoalsBinding: ActivityAddGoalsBinding? = null
    private val binding get() = activityAddGoalsBinding!!

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddGoalsBinding = ActivityAddGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ValueGoalsActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }

}