package com.brainoptimax.peakmeup.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.brainoptimax.peakmeup.ui.breathing.MainBreathingActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityIntroBreathingBinding

class IntroBreathingActivity : AppCompatActivity() {

    private var activityIntroBreathingBinding: ActivityIntroBreathingBinding? = null
    private val binding get() = activityIntroBreathingBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_breathing)

        activityIntroBreathingBinding = ActivityIntroBreathingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStartBreathing.setOnClickListener {
            startActivity(Intent(this, MainBreathingActivity::class.java))
            Animatoo.animateSlideUp(this)
            finish()
        }

    }
}