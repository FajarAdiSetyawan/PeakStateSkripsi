package com.brainoptimax.peakmeup.ui.emotion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityChooseEmotionsBinding
import com.brainoptimax.peakmeup.ui.emotion.fragment.NegativeEmotionFragment
import com.brainoptimax.peakmeup.ui.emotion.fragment.PositiveEmotionFragment
import com.brainoptimax.peakmeup.utils.Animatoo

class ChooseEmotionsActivity : AppCompatActivity() {

    private var activityChooseEmotionsBinding: ActivityChooseEmotionsBinding? = null
    private val binding get() = activityChooseEmotionsBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)

        activityChooseEmotionsBinding = ActivityChooseEmotionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(PositiveEmotionFragment(), resources.getString(R.string.positive))
        adapter.addFrag(NegativeEmotionFragment(), resources.getString(R.string.negative))
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EmotionGaugeActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}