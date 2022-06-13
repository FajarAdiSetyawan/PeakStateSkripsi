package com.brainoptimax.peakmeup.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.brainoptimax.peakmeup.adapter.intro.IntroSliderAnchoringAdapter
import com.brainoptimax.peakmeup.ui.anchoring.AnchoringActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityIntroAnchoringBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IntroAnchoringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroAnchoringBinding
    private lateinit var mViewPager: ViewPager2

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroAnchoringBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        loadTheme()
        mViewPager = binding.viewPager
        mViewPager.adapter = IntroSliderAnchoringAdapter(this, this)
        mViewPager.offscreenPageLimit = 1
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 6) {
                    binding.btnNextStep.visibility = View.GONE
                    binding.textEnd.visibility = View.VISIBLE
                    binding.textSkip.visibility = View.GONE
                } else {
                    binding.btnNextStep.visibility = View.VISIBLE
                    binding.textEnd.visibility = View.GONE
                    binding.textSkip.visibility = View.VISIBLE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })



        binding.textSkip.setOnClickListener {
            finish()
            val intent =
                Intent(applicationContext, AnchoringActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding.textEnd.setOnClickListener {
            finish()
            val intent =
                Intent(applicationContext, AnchoringActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }


        binding.btnNextStep.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                finish()
                val intent =
                    Intent(applicationContext, AnchoringActivity::class.java)
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