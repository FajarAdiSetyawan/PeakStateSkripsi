package com.brainoptimax.peakmeup.ui.emotion.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.adapter.emotions.ResultPositiveAdapter
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.databinding.FragmentResultPositiveBinding
import com.brainoptimax.peakmeup.utils.Preferences


class ResultPositiveFragment : Fragment() {

    private var fragmentResultPositiveBinding: FragmentResultPositiveBinding? = null
    private val binding get() = fragmentResultPositiveBinding!!

    private lateinit var viewModel: EmotionViewModel
    private var resultPositiveAdapter: ResultPositiveAdapter? = null

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultPositiveBinding = FragmentResultPositiveBinding.inflate(layoutInflater, container, false)
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
        binding.rvEmotionsPositive.layoutManager = layoutManager
        binding.rvEmotionsPositive.hasFixedSize()
        viewModel.allPositive(uidUser!!)
        viewModel.positiveMutableLiveData.observe(requireActivity()) { emotions ->
            Log.d("TAG", "onDataChangePositive: $emotions")
            goneLoading()

            if (emotions!!.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvEmotionsPositive.visibility = View.INVISIBLE
            } else {
                binding.layoutEmpty.visibility = View.INVISIBLE
                resultPositiveAdapter = ResultPositiveAdapter(emotions, requireActivity())
                binding.rvEmotionsPositive.adapter = resultPositiveAdapter
                resultPositiveAdapter!!.notifyDataSetChanged()
                binding.rvEmotionsPositive.visibility = View.VISIBLE
            }
        }

        viewModel.databaseErrorPositive.observe(requireActivity()) { error ->
            goneLoading()
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }


//
    }

    private fun showLoading(){
        binding.pbEmotions.visibility = View.VISIBLE
        binding.rvEmotionsPositive.visibility = View.INVISIBLE
    }

    private fun goneLoading(){
        binding.pbEmotions.visibility = View.INVISIBLE
        binding.rvEmotionsPositive.visibility = View.VISIBLE
    }


}