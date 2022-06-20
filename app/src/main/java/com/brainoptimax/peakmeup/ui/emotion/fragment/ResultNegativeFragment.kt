package com.brainoptimax.peakmeup.ui.emotion.fragment

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
import com.brainoptimax.peakmeup.adapter.emotions.ResultNegativeAdapter
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.databinding.FragmentResultNegativeBinding
import com.brainoptimax.peakmeup.utils.Preferences


class ResultNegativeFragment : Fragment() {

    private var fragmentResultNegativeBinding: FragmentResultNegativeBinding? = null
    private val binding get() = fragmentResultNegativeBinding!!

    private lateinit var viewModel: EmotionViewModel
    private var resultNegativeAdapter: ResultNegativeAdapter? = null

    private lateinit var preference: Preferences

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

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        showLoading()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rvEmotionsNegative.layoutManager = layoutManager
        binding.rvEmotionsNegative.hasFixedSize()

        viewModel.allNegative(uidUser!!)
        viewModel.negativeMutableLiveData.observe(requireActivity()) { emotions ->
            Log.d("TAG", "onDataChangeNegative: $emotions")
            goneLoading()

            if (emotions!!.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvEmotionsNegative.visibility = View.INVISIBLE
            } else {
                binding.layoutEmpty.visibility = View.INVISIBLE
                resultNegativeAdapter = ResultNegativeAdapter(emotions, requireActivity())
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