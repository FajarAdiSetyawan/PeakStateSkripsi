package com.brainoptimax.peakstate.ui.emotion.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.emotions.DailyEmotionAdapter
import com.brainoptimax.peakstate.databinding.FragmentDailyEmotionBinding
import com.brainoptimax.peakstate.viewmodel.emotion.EmotionViewModel
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import org.joda.time.DateTime
import java.text.SimpleDateFormat

class DailyEmotionFragment : Fragment(), DatePickerListener {
    private var fragmentDailyEmotionBinding: FragmentDailyEmotionBinding? = null
    private val binding get() = fragmentDailyEmotionBinding!!

    private var emotionDayAdapter: DailyEmotionAdapter? = null

    private lateinit var viewModel: EmotionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDailyEmotionBinding =
            FragmentDailyEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[EmotionViewModel::class.java]


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvEmotion.layoutManager = layoutManager
        binding.rvEmotion.hasFixedSize()

        // find the picker
        val picker = binding.datePicker
        // initialize it and attach a listener
        picker.setListener(this)
            .setDays(14)
            .setOffset(7)
            .setDateSelectedColor(resources.getColor(R.color.md_yellow_600))
            .setDateSelectedTextColor(Color.WHITE)
            .setMonthAndYearTextColor(Color.DKGRAY)
            .setTodayButtonTextColor(resources.getColor(R.color.colorPrimary))
            .setTodayDateTextColor(resources.getColor(R.color.white))
            .setTodayDateBackgroundColor(Color.GRAY)
            .setUnselectedDayTextColor(Color.DKGRAY)
            .setDayOfWeekTextColor(Color.DKGRAY)
            .setUnselectedDayTextColor(resources.getColor(R.color.md_blue_grey_200))
            .showTodayButton(true)
            .init()
        picker.backgroundColor = resources.getColor(R.color.bg_layout)
        picker.setDate(DateTime().plusDays(0))
    }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun onDateSelected(dateSelected: DateTime?) {
        //merubah format tanggal
        val result = dateSelected!!.toDate()
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy")
        val day = sdf.format(result)


        binding.progressBarEmotion.visibility = View.VISIBLE
        viewModel.allEmotions(day)
        viewModel.emotionMutableLiveData.observe(requireActivity()){ emotions ->
            Log.d("TAG", "onDataChangeGoals: $emotions")
            binding.progressBarEmotion.visibility = View.INVISIBLE

            if (emotions!!.isEmpty()){
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvEmotion.visibility = View.INVISIBLE
            }else{
                binding.layoutEmpty.visibility = View.INVISIBLE
                emotionDayAdapter = DailyEmotionAdapter(emotions)
                binding.rvEmotion.adapter = emotionDayAdapter
                emotionDayAdapter!!.notifyDataSetChanged()
                binding.rvEmotion.visibility = View.VISIBLE
            }
        }

        viewModel.databaseErrorEmotions.observe(requireActivity()
        ) { error ->
            binding.progressBarEmotion.visibility = View.INVISIBLE
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

    }


}

