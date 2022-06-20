package com.brainoptimax.peakmeup.ui.quiz.fragment

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
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.quiz.ResultEnergyQuizAdapter
import com.brainoptimax.peakmeup.adapter.quiz.ResultPeakQuizAdapter
import com.brainoptimax.peakmeup.databinding.FragmentResultQuizPeakBinding
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ResultQuizPeakFragment : Fragment() {

    private var fragmentResultQuizPeakBinding: FragmentResultQuizPeakBinding? = null
    private val binding get() = fragmentResultQuizPeakBinding!!

    private lateinit var viewModel: QuizViewModel
    private var resultPeakQuizAdapter: ResultPeakQuizAdapter? = null
    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultQuizPeakBinding = FragmentResultQuizPeakBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        showLoading()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvResultPeak.layoutManager = layoutManager
        resultPeakQuizAdapter = ResultPeakQuizAdapter(requireActivity())
        binding.rvResultPeak.adapter = resultPeakQuizAdapter

        viewModel.getResultPeak(uidUser!!)
        viewModel.peakQuizMutableLiveData.observe(requireActivity()) { peak ->
            Log.d("TAG", "onDataChangePeak: $peak")
            goneLoading()

            if (peak!!.isEmpty()){
                binding.rvResultPeak.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            }else{
                binding.rvResultPeak.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.INVISIBLE
            }
            resultPeakQuizAdapter!!.setPeakQuiz(peak)
            resultPeakQuizAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorPeak.observe(requireActivity()) { error ->
            goneLoading()
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        resultPeakQuizAdapter!!.setOnItemClickListener { quiz ->
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) + " ${quiz.peakQuiz} ?")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.deletePeakQuiz(uidUser, quiz.idQuiz!!)
                    Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " ${quiz.peakQuiz}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, which -> }
                .show()
        }
    }

    private fun showLoading() {
        binding.pbResultPeak.visibility = View.VISIBLE
        binding.rvResultPeak.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.pbResultPeak.visibility = View.INVISIBLE
        binding.rvResultPeak.visibility = View.VISIBLE
    }
}