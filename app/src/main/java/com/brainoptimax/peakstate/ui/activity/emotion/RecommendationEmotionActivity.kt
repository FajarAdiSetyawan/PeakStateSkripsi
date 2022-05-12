package com.brainoptimax.peakstate.ui.activity.emotion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityRecommendationEmotionBinding

class RecommendationEmotionActivity : AppCompatActivity() {

    private var activityRecommendationEmotionBinding: ActivityRecommendationEmotionBinding? = null
    private val binding get() = activityRecommendationEmotionBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRecommendationEmotionBinding = ActivityRecommendationEmotionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)
    }
}