package com.brainoptimax.peakmeup.ui.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityResultQuizListBinding
import com.brainoptimax.peakmeup.ui.emotion.EmotionGaugeActivity
import com.brainoptimax.peakmeup.ui.emotion.fragment.ResultNegativeFragment
import com.brainoptimax.peakmeup.ui.emotion.fragment.ResultPositiveFragment
import com.brainoptimax.peakmeup.ui.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakmeup.ui.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakmeup.ui.quiz.fragment.ResultQuizEnergyFragment
import com.brainoptimax.peakmeup.ui.quiz.fragment.ResultQuizPeakFragment
import com.brainoptimax.peakmeup.utils.Animatoo

class ResultQuizListActivity : AppCompatActivity() {

    private var activityResultQuizListBinding: ActivityResultQuizListBinding? = null
    private val binding get() = activityResultQuizListBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultQuizListBinding = ActivityResultQuizListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.ivClose.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
            Animatoo.animateSlideDown(this)
            finish()
        }


        binding.btnPeak.setOnClickListener {
            startActivity(Intent(this, IntroPeakStateQuizActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        binding.btnEnergy.setOnClickListener {
            startActivity(Intent(this, IntroEnergyTensionActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(ResultQuizPeakFragment(), resources.getString(R.string.pref_peak_title))
        adapter.addFrag(ResultQuizEnergyFragment(), resources.getString(R.string.pref_energy_title))
        viewPager.adapter = adapter
    }

    internal class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}