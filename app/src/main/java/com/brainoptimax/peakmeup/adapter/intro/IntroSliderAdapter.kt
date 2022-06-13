package com.brainoptimax.peakmeup.adapter.intro

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brainoptimax.peakmeup.ui.intro.IntroSliderFragment
import com.brainoptimax.peakmeup.R

class IntroSliderAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_slider_1),
                context.resources.getString(R.string.description_intro_slider_1),
                R.drawable.intro_main_1
            )
            1 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_slider_2),
                context.resources.getString(R.string.description_intro_slider_2),
                R.drawable.intro_main_2
            )
            2 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_slider_3),
                context.resources.getString(R.string.description_intro_slider_3),
                R.drawable.intro_main_3
            )
            else -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_slider_4),
                context.resources.getString(R.string.description_intro_slider_4),
                R.drawable.intro_main_4
            )
        }
    }

    override fun getItemCount(): Int {
        return 4
    }


}