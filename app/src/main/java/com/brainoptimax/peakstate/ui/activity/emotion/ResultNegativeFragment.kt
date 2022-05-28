package com.brainoptimax.peakstate.ui.activity.emotion

import android.annotation.SuppressLint
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
import com.brainoptimax.peakstate.adapter.emotions.DailyEmotionAdapter
import com.brainoptimax.peakstate.adapter.emotions.ResultNegativeAdapter
import com.brainoptimax.peakstate.databinding.FragmentResultNegativeBinding
import com.brainoptimax.peakstate.viewmodel.emotion.EmotionViewModel
import com.google.firebase.database.*


class ResultNegativeFragment : Fragment() {

    private var fragmentResultNegativeBinding: FragmentResultNegativeBinding? = null
    private val binding get() = fragmentResultNegativeBinding!!

    private lateinit var viewModel: EmotionViewModel
    private var resultNegativeAdapter: ResultNegativeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultNegativeBinding =
            FragmentResultNegativeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[EmotionViewModel::class.java]

        showLoading()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rvEmotionsNegative.layoutManager = layoutManager
        binding.rvEmotionsNegative.hasFixedSize()
        viewModel.allNegative
        viewModel.negativeMutableLiveData.observe(requireActivity()) { emotions ->
            Log.d("TAG", "onDataChangeNegative: $emotions")
            goneLoading()

            if (emotions!!.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvEmotionsNegative.visibility = View.INVISIBLE
            } else {
                binding.layoutEmpty.visibility = View.INVISIBLE
                resultNegativeAdapter = ResultNegativeAdapter(emotions)
                binding.rvEmotionsNegative.adapter = resultNegativeAdapter
                resultNegativeAdapter!!.notifyDataSetChanged()
                binding.rvEmotionsNegative.visibility = View.VISIBLE
            }
        }

        viewModel.databaseErrorNegative.observe(requireActivity()) { error ->
            goneLoading()
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading() {
        binding.pbEmotions.visibility = View.VISIBLE
        binding.rvEmotionsNegative.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.pbEmotions.visibility = View.INVISIBLE
        binding.rvEmotionsNegative.visibility = View.VISIBLE
    }


}