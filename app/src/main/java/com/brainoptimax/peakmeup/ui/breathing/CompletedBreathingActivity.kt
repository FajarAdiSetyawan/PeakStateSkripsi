package com.brainoptimax.peakmeup.ui.breathing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityCompletedBreathingBinding

class CompletedBreathingActivity : AppCompatActivity() {

    private var activityCompletedBreathingBinding: ActivityCompletedBreathingBinding? = null
    private val binding get() = activityCompletedBreathingBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCompletedBreathingBinding = ActivityCompletedBreathingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_breathing)

        performAnimation(binding.bg1, 0.86f, 2000)
        performAnimation(binding.bg2, 0.8f, 2000)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, MainBreathingActivity::class.java))
            Animatoo.animateFade(this)
            finish()
        }
    }

    private fun performAnimation(im: ImageView?, f: Float, timer: Int) {
        val scaleDownX = ObjectAnimator.ofFloat(im, "scaleX", f)
        val scaleDownY = ObjectAnimator.ofFloat(im, "scaleY", f)
        scaleDownX.duration = timer.toLong()
        scaleDownY.duration = timer.toLong()
        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        //scaleDown.setStartDelay(hold);
        scaleDown.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainBreathingActivity::class.java))
        Animatoo.animateFade(this)
        finish()
    }

}