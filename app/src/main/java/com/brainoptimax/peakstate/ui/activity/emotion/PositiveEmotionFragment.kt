package com.brainoptimax.peakstate.ui.activity.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.emotions.PositiveEmotionAdapter
import com.brainoptimax.peakstate.databinding.FragmentPositiveEmotionBinding
import com.brainoptimax.peakstate.model.ListEmotions


class PositiveEmotionFragment : Fragment() {

    private var fragmentPositiveEmotionBinding: FragmentPositiveEmotionBinding? = null
    private val binding get() = fragmentPositiveEmotionBinding!!

    private lateinit var positiveEmotionAdapter: PositiveEmotionAdapter
    private var emotionList = mutableListOf<ListEmotions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPositiveEmotionBinding =
            FragmentPositiveEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEmotionsPositive.layoutManager =
            GridLayoutManager(activity!!.applicationContext, 3)
        positiveEmotionAdapter = PositiveEmotionAdapter(activity!!.applicationContext)
        binding.rvEmotionsPositive.adapter = positiveEmotionAdapter

        //add data
        emotionList.add(ListEmotions(resources.getString(R.string.excited), R.drawable.ic_exited))
        emotionList.add(ListEmotions(resources.getString(R.string.enthusiastic), R.drawable.ic_enthusiastic))
        emotionList.add(ListEmotions(resources.getString(R.string.happy), R.drawable.ic_happy))
        emotionList.add(ListEmotions(resources.getString(R.string.grateful), R.drawable.ic_grateful))
        emotionList.add(ListEmotions(resources.getString(R.string.passionate), R.drawable.ic_passionate))
        emotionList.add(ListEmotions(resources.getString(R.string.joyful), R.drawable.ic_joyful))
        emotionList.add(ListEmotions(resources.getString(R.string.brave), R.drawable.ic_brave))
        emotionList.add(ListEmotions(resources.getString(R.string.confident), R.drawable.ic_confident))
        emotionList.add(ListEmotions(resources.getString(R.string.proud), R.drawable.ic_proud))
        emotionList.add(ListEmotions(resources.getString(R.string.hopeful), R.drawable.ic_hopeful))
        emotionList.add(ListEmotions(resources.getString(R.string.optimistic), R.drawable.ic_optimistic))
        emotionList.add(ListEmotions(resources.getString(R.string.calm), R.drawable.ic_calm))
        emotionList.add(ListEmotions(resources.getString(R.string.`fun`), R.drawable.ic_fun))
        emotionList.add(ListEmotions(resources.getString(R.string.awful), R.drawable.ic_awful))
        emotionList.add(ListEmotions(resources.getString(R.string.good), R.drawable.ic_good))
        positiveEmotionAdapter.setDataList(emotionList)

    }


}