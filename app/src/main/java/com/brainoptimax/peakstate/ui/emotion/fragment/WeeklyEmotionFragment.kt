package com.brainoptimax.peakstate.ui.emotion.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakstate.databinding.FragmentWeeklyEmotionBinding
import com.brainoptimax.peakstate.ui.emotion.ResultEmotionActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.emotion.EmotionViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyEmotionFragment : Fragment() {

    private var fragmentWeeklyEmotionBinding: FragmentWeeklyEmotionBinding? = null
    private val binding get() = fragmentWeeklyEmotionBinding!!
    private lateinit var viewModel: EmotionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentWeeklyEmotionBinding = FragmentWeeklyEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[EmotionViewModel::class.java]

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        binding.tvMonthEmotion.text = currentDateAndTime

        viewModel.totalAllEmotion
        viewModel.totalAllEmotionMutableLiveData.observe(requireActivity()) { totalAllEmotion ->
            Log.d("TAG", "totalAllEmotion: $totalAllEmotion")

            if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                binding.tvTotalEmotion.text = "0 Emotion"
            } else {
                binding.tvTotalEmotion.text = "$totalAllEmotion Emotions"
            }
        }

        binding.cardWeek.setOnClickListener {
            val choose = Intent(requireActivity(), ResultEmotionActivity::class.java)
            startActivity(choose)
            Animatoo.animateSlideUp(requireActivity())
        }
    }


}