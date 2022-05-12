package com.brainoptimax.peakstate.ui.activity.emotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.emotions.NegativeEmotionAdapter
import com.brainoptimax.peakstate.adapter.emotions.PositiveEmotionAdapter
import com.brainoptimax.peakstate.databinding.FragmentNegativeEmotionBinding
import com.brainoptimax.peakstate.model.ListEmotions


class NegativeEmotionFragment : Fragment() {

    private var fragmentNegativeEmotionBinding: FragmentNegativeEmotionBinding? = null
    private val binding get() = fragmentNegativeEmotionBinding!!

    private lateinit var negativeEmotionAdapter: NegativeEmotionAdapter
    private var emotionList = mutableListOf<ListEmotions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentNegativeEmotionBinding = FragmentNegativeEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEmotionsNegative.layoutManager =
            GridLayoutManager(activity!!.applicationContext, 3)
        negativeEmotionAdapter = NegativeEmotionAdapter(activity!!.applicationContext)
        binding.rvEmotionsNegative.adapter = negativeEmotionAdapter

        //add data
        emotionList.add(ListEmotions(resources.getString(R.string.numb), R.drawable.ic_numb))
        emotionList.add(ListEmotions(resources.getString(R.string.bad), R.drawable.ic_bad))
        emotionList.add(ListEmotions(resources.getString(R.string.bored), R.drawable.ic_bored))
        emotionList.add(ListEmotions(resources.getString(R.string.tired), R.drawable.ic_tired))
        emotionList.add(ListEmotions(resources.getString(R.string.frustrated), R.drawable.ic_frustrated))
        emotionList.add(ListEmotions(resources.getString(R.string.stressed), R.drawable.ic_stressed))
        emotionList.add(ListEmotions(resources.getString(R.string.insecure), R.drawable.ic_insecure))
        emotionList.add(ListEmotions(resources.getString(R.string.angry), R.drawable.ic_angry))
        emotionList.add(ListEmotions(resources.getString(R.string.sad), R.drawable.ic_sad))
        emotionList.add(ListEmotions(resources.getString(R.string.afraid), R.drawable.ic_afraid))
        emotionList.add(ListEmotions(resources.getString(R.string.envious), R.drawable.ic_envious))
        emotionList.add(ListEmotions(resources.getString(R.string.anxious), R.drawable.ic_anxious))
        emotionList.add(ListEmotions(resources.getString(R.string.down), R.drawable.ic_down))
        negativeEmotionAdapter.setDataList(emotionList)

    }


}