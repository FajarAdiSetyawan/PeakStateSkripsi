package com.brainoptimax.peakmeup.ui.emotion

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.brainoptimax.peakmeup.ui.emotion.fragment.ResultNegativeFragment
import com.brainoptimax.peakmeup.ui.emotion.fragment.ResultPositiveFragment
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityResultEmotionBinding
import com.brainoptimax.peakmeup.utils.Preferences

class ResultEmotionActivity : AppCompatActivity() {

    private var activityResultEmotionBinding: ActivityResultEmotionBinding? = null
    private val binding get() = activityResultEmotionBinding!!
    private lateinit var viewModel: EmotionViewModel
    private lateinit var preference: Preferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[EmotionViewModel::class.java]
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)

        preference = Preferences(this)
        val uidUser = preference.getValues("uid")

        activityResultEmotionBinding = ActivityResultEmotionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.totalAllEmotion(uidUser!!)
        viewModel.totalAllEmotionMutableLiveData.observe(this) { totalAllEmotion ->
            Log.d("TAG", "totalAllEmotion: $totalAllEmotion")

            if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                binding.tvTotal.text = "0 " + resources.getString(R.string.emotion)
            } else {
                binding.tvTotal.text = "$totalAllEmotion " + resources.getString(R.string.emotion)
            }
        }

        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.btnRecomendation.setOnClickListener {
            startActivity(Intent(this, RecommendationEmotionActivity::class.java))
            Animatoo.animateCard(this)
            finish()
        }

        binding.ivClose.setOnClickListener {
            startActivity(Intent(this, EmotionGaugeActivity::class.java))
            Animatoo.animateSlideDown(this)
            finish()
        }

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(ResultPositiveFragment(), resources.getString(R.string.positive))
        adapter.addFrag(ResultNegativeFragment(), resources.getString(R.string.negative))
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