package com.brainoptimax.peakstate.ui.activity.anchoring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAnchoringBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroAnchoringActivity
import com.brainoptimax.peakstate.utils.Animatoo

class AnchoringActivity : AppCompatActivity() {

    private var activityAnchoringBinding: ActivityAnchoringBinding? = null
    private val binding get() = activityAnchoringBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAnchoringBinding = ActivityAnchoringBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        binding.btnStart.setOnClickListener {
            val intent =
                Intent(this, Anchoring1Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding.ivInfo.setOnClickListener {
            val intent =
                Intent(this, IntroAnchoringActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSwipeRight(this)
        }

        binding.btnLast.setOnClickListener {
            startActivity(Intent(this, ResultAnchoringActivity::class.java))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            Animatoo.animateSlideLeft(this)
            finish()
        }
    }
}