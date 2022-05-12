package com.brainoptimax.peakstate.ui.activity.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.intro.IntroSliderAdapter
import com.brainoptimax.peakstate.databinding.ActivityIntroSliderBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.android.material.tabs.TabLayoutMediator
import render.animations.Render
import render.animations.Slide

class IntroSliderActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager2
    private lateinit var binding: ActivityIntroSliderBinding
    private var doubleBackToExitPressedOnce = false

    private val animation = Render(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()
        binding = ActivityIntroSliderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        animation.setAnimation(Slide.InRight(binding.btnNextStep))
        animation.start()

        animation.setAnimation(Slide.InLeft(binding.pageIndicator))
        animation.start()

        animation.setAnimation(Slide.InDown(binding.textSkip))
        animation.start()

        mViewPager = binding.viewPager
        mViewPager.adapter = IntroSliderAdapter(this, this)
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        binding.textSkip.setOnClickListener {
            finish()
            val intent =
                Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }


        binding.btnNextStep.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                finish()
                val intent =
                    Intent(applicationContext, MainActivity::class.java)
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

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
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