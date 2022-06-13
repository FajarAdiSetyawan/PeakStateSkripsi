package com.brainoptimax.peakmeup.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.brainoptimax.peakmeup.adapter.intro.IntroSliderEmotionGaugeAdapter
import com.brainoptimax.peakmeup.ui.emotion.EmotionGaugeActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityIntroEmotionBinding
import com.google.android.material.tabs.TabLayoutMediator
import render.animations.Render
import render.animations.Slide

class IntroEmotionActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var binding: ActivityIntroEmotionBinding
    private var doubleBackToExitPressedOnce = false

    private val animation = Render(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroEmotionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)
        loadTheme()

        animation.setAnimation(Slide.InRight(binding.btnNextStep))
        animation.start()

        animation.setAnimation(Slide.InLeft(binding.pageIndicator))
        animation.start()

        animation.setAnimation(Slide.InDown(binding.textSkip))
        animation.start()

        mViewPager = binding.viewPager
        mViewPager.adapter = IntroSliderEmotionGaugeAdapter(this, this)
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        binding.textSkip.setOnClickListener {
            finish()
            val intent =
                Intent(applicationContext, EmotionGaugeActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }


        binding.btnNextStep.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                finish()
                val intent =
                    Intent(applicationContext, EmotionGaugeActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }
    }
    private fun getItem(): Int {
        return mViewPager.currentItem
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.doubleback, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun loadTheme() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val theme = preference.getString(
            getString(R.string.pref_key_theme),
            getString(R.string.pref_theme_entry_auto)
        )
        when {
            theme.equals(getString(R.string.pref_theme_entry_auto)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            theme.equals(getString(R.string.pref_theme_entry_dark)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            theme.equals(getString(R.string.pref_theme_entry_light)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateTheme(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        return true
    }
}