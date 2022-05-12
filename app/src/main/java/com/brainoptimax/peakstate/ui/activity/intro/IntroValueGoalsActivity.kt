package com.brainoptimax.peakstate.ui.activity.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brainoptimax.peakstate.databinding.ActivityIntroValueGoalsBinding

class IntroValueGoalsActivity : AppCompatActivity() {

    private var activityIntroValueGoalsBinding: ActivityIntroValueGoalsBinding? = null
    private val binding get() = activityIntroValueGoalsBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityIntroValueGoalsBinding = ActivityIntroValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}