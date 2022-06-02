package com.brainoptimax.peakstate.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.databinding.FragmentIntroSliderBinding

class IntroSliderFragment : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var imageResource = 0
    private lateinit var binding: FragmentIntroSliderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = requireArguments().getString(ARG_PARAM1)!!
            description = requireArguments().getString(ARG_PARAM2)!!
            imageResource = requireArguments().getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIntroSliderBinding.inflate(inflater, container, false)
        binding.textIntroSliderTitle.text = title
        binding.textIntroSliderDescription.text = description
        binding.imageOnboarding.setImageResource(imageResource)


        val desc = binding.textIntroSliderDescription.text.toString()

        if (desc == "" || desc.isEmpty()){
            binding.imageOnboarding.layoutParams.height = 920
            binding.imageOnboarding.layoutParams.width = 900
            binding.textIntroSliderDescription.visibility = View.VISIBLE
        }

        return binding.root
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        fun newInstance(
            title: String?,
            description: String?,
            imageResource: Int
        ): IntroSliderFragment {
            val fragment = IntroSliderFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, title)
            args.putString(ARG_PARAM2, description)
            args.putInt(ARG_PARAM3, imageResource)
            fragment.arguments = args
            return fragment
        }
    }


}