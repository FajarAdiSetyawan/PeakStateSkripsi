package com.brainoptimax.peakmeup.adapter.intro

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brainoptimax.peakmeup.ui.intro.IntroSliderFragment
import com.brainoptimax.peakmeup.R

class IntroSliderPeakQuizAdapter (
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_psq_1),
                context.resources.getString(R.string.description_intro_psq_1),
                R.drawable.peakstate_logo
            )
            else -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_psq_2),
                context.resources.getString(R.string.description_intro_psq_2),
                R.drawable.intro_psq_2
            )
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}