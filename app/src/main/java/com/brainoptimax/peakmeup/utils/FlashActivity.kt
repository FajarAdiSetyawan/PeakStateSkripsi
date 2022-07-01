package com.brainoptimax.peakmeup.utils

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.brainoptimax.peakmeup.R

class FlashActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        overridePendingTransition(
            R.anim.animation_localization_activity_transition_in,
            R.anim.animation_localization_activity_transition_out
        )
        setContentView(R.layout.activity_blank_dummy)
        delayedFinish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.animation_localization_activity_transition_in,
            R.anim.animation_localization_activity_transition_out
        )
    }

    private fun delayedFinish() {
        Handler().postDelayed({ finish() }, 200)
    }
}