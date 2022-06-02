package com.brainoptimax.peakstate.adapter.intro

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.ui.intro.IntroSliderFragment

class IntroSliderAnchoringAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_1),
                context.resources.getString(R.string.description_intro_anchor_1),
                R.drawable.ic_intro_anchoring_1
            )
            1 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_2),
                context.resources.getString(R.string.description_intro_anchor_2),
                R.drawable.ic_intro_anchoring_2
            )
            2 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_3),
                context.resources.getString(R.string.description_intro_anchor_3),
                R.drawable.ic_intro_anchoring_3
            )
            3 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_4),
                context.resources.getString(R.string.description_intro_anchor_4),
                R.drawable.ic_intro_anchoring_4
            )
            4 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_5),
                context.resources.getString(R.string.description_intro_anchor_5),
                R.drawable.ic_intro_anchoring_5
            )
            5 -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_6),
                context.resources.getString(R.string.blank),
                R.drawable.ic_intro_anchoring_6
            )
            else -> IntroSliderFragment.newInstance(
                context.resources.getString(R.string.title_intro_anchor_7),
                context.resources.getString(R.string.blank),
                R.drawable.ic_intro_anchoring_7
            )
        }
    }

    override fun getItemCount(): Int {
        return 7
    }
}