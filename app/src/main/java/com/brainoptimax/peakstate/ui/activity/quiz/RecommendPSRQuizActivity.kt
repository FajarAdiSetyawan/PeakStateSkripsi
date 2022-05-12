package com.brainoptimax.peakstate.ui.activity.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityRecommendPsrquizBinding
import com.brainoptimax.peakstate.utils.Animatoo

class RecommendPSRQuizActivity : AppCompatActivity() {

    private var activityRecommendPsrquizBinding: ActivityRecommendPsrquizBinding? = null
    private val binding get() = activityRecommendPsrquizBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRecommendPsrquizBinding = ActivityRecommendPsrquizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_tosca)
        val intent = intent
        val result = intent.getStringExtra("result")

        when {
            result.equals("Low") -> {
                binding.tvTextCard1.text = resources.getString(R.string.low_readiness)
                binding.expandText1.text = resources.getString(R.string.recommend_low_1)
                binding.expandText2.text = resources.getString(R.string.recommend_low_2)
                binding.expandText3.text = resources.getString(R.string.recommend_low_3)
                binding.expandText4.text = resources.getString(R.string.recommend_low_4)
                binding.expandText5.text = resources.getString(R.string.recommend_low_5)
                binding.expandText6.text = resources.getString(R.string.recommend_low_6)
                binding.expandText7.text = resources.getString(R.string.recommend_low_7)
                binding.expandText8.text = resources.getString(R.string.recommend_low_8)
                binding.expandText9.text = resources.getString(R.string.recommend_low_9)
                binding.expandText10.text = resources.getString(R.string.recommend_low_10)
                binding.expandText11.text = resources.getString(R.string.recommend_low_11)
            }
            result.equals("Medium") -> {
                binding.tvTextCard1.text = resources.getString(R.string.med_readiness)
                binding.expandText1.text = resources.getString(R.string.recommend_med_1)
                binding.expandText2.text = resources.getString(R.string.recommend_med_2)
                binding.expandText3.text = resources.getString(R.string.recommend_med_3)
                binding.expandText4.text = resources.getString(R.string.recommend_med_4)
                binding.expandText5.text = resources.getString(R.string.recommend_med_5)
                binding.expandText6.text = resources.getString(R.string.recommend_med_6)
                binding.expandText7.text = resources.getString(R.string.recommend_med_7)
                binding.expandText8.text = resources.getString(R.string.recommend_med_8)
                binding.expandText9.text = resources.getString(R.string.recommend_med_9)
                binding.expandText10.text = resources.getString(R.string.recommend_med_10)
                binding.expandText11.text = resources.getString(R.string.recommend_med_11)
            }
            result.equals("High") -> {
                binding.tvTextCard1.text = resources.getString(R.string.high_readiness)
                binding.expandText1.text = resources.getString(R.string.recommend_high_1)
                binding.expandText2.text = resources.getString(R.string.recommend_high_2)
                binding.expandText3.text = resources.getString(R.string.recommend_high_3)
                binding.expandText4.text = resources.getString(R.string.recommend_high_4)
                binding.expandText5.text = resources.getString(R.string.recommend_high_5)
                binding.expandText6.text = resources.getString(R.string.recommend_high_6)
                binding.expandText7.text = resources.getString(R.string.recommend_high_7)
                binding.expandText8.text = resources.getString(R.string.recommend_high_8)
                binding.expandText9.text = resources.getString(R.string.recommend_high_9)
                binding.expandText10.text = resources.getString(R.string.recommend_high_10)
                binding.expandText11.text = resources.getString(R.string.recommend_high_11)
            }
        }

        binding.btnBackMenu.setOnClickListener {
            super.onBackPressed()
            startActivity(Intent(this, ResultPSRQuizActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ResultPSRQuizActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }
}