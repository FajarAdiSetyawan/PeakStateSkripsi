package com.brainoptimax.peakmeup.ui.emotion.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakmeup.ui.emotion.ResultEmotionActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentWeeklyEmotionBinding
import com.brainoptimax.peakmeup.utils.Preferences
import java.text.SimpleDateFormat
import java.util.*

class WeeklyEmotionFragment : Fragment() {

    private var fragmentWeeklyEmotionBinding: FragmentWeeklyEmotionBinding? = null
    private val binding get() = fragmentWeeklyEmotionBinding!!
    private lateinit var viewModel: EmotionViewModel

    private lateinit var preference: Preferences


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

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        binding.tvMonthEmotion.text = currentDateAndTime

        viewModel.totalAllEmotion(uidUser!!)
        viewModel.totalAllEmotionMutableLiveData.observe(requireActivity()) { totalAllEmotion ->
            Log.d("TAG", "totalAllEmotion: $totalAllEmotion")

            if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                binding.tvTotalEmotion.text = "0 " + resources.getString(R.string.emotion)
            } else {
                binding.tvTotalEmotion.text = "$totalAllEmotion " + resources.getString(R.string.emotion)
            }
        }

        binding.cardWeek.setOnClickListener {
            val choose = Intent(requireActivity(), ResultEmotionActivity::class.java)
            startActivity(choose)
            Animatoo.animateSlideUp(requireActivity())
        }
    }


}