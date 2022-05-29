package com.brainoptimax.peakstate.ui.activity.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brainoptimax.peakstate.databinding.ActivityIntroValueGoalsBinding
import com.brainoptimax.peakstate.ui.activity.goals.ValueGoalsActivity
import com.brainoptimax.peakstate.utils.Animatoo

class IntroValueGoalsActivity : AppCompatActivity() {

    private var activityIntroValueGoalsBinding: ActivityIntroValueGoalsBinding? = null
    private val binding get() = activityIntroValueGoalsBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityIntroValueGoalsBinding = ActivityIntroValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, ValueGoalsActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }
    }
}