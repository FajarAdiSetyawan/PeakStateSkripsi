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
import com.brainoptimax.peakmeup.adapter.anchoring.AnchoringAdapter
import com.brainoptimax.peakmeup.adapter.emotions.ResultNegativeAdapter
import com.brainoptimax.peakmeup.adapter.quiz.ResultEnergyQuizAdapter
import com.brainoptimax.peakmeup.databinding.FragmentResultQuizEnergyBinding
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ResultQuizEnergyFragment : Fragment() {

    private var fragmentResultQuizEnergyBinding: FragmentResultQuizEnergyBinding? = null
    private val binding get() = fragmentResultQuizEnergyBinding!!

    private lateinit var viewModel: QuizViewModel
    private var resultEnergyQuizAdapter: ResultEnergyQuizAdapter? = null
    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultQuizEnergyBinding = FragmentResultQuizEnergyBinding.inflate(layoutInflater, container, false)
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
        binding.rvResultEnergy.layoutManager = layoutManager
        resultEnergyQuizAdapter = ResultEnergyQuizAdapter(requireActivity())
        binding.rvResultEnergy.adapter = resultEnergyQuizAdapter

        viewModel.getResultEnergy(uidUser!!)
        viewModel.energyQuizMutableLiveData.observe(viewLifecycleOwner) { energy ->
            Log.d("TAG", "onDataChangeEnergy: $energy")
            goneLoading()
            if (energy!!.isEmpty()){
                binding.rvResultEnergy.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            }else{
                binding.rvResultEnergy.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.INVISIBLE
            }
            resultEnergyQuizAdapter!!.setEnergyQuiz(energy)
            resultEnergyQuizAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorEnergy.observe(viewLifecycleOwner) { error ->
            goneLoading()
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        resultEnergyQuizAdapter!!.setOnItemClickListener { quiz ->
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) + " ${quiz.energyQuiz} ?")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.deleteEnergyQuiz(uidUser, quiz.idQuiz!!)
                    Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " ${quiz.energyQuiz}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, which -> }
                .show()
        }
    }

    private fun showLoading() {
        binding.pbResultEnergy.visibility = View.VISIBLE
        binding.rvResultEnergy.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.pbResultEnergy.visibility = View.INVISIBLE
        binding.rvResultEnergy.visibility = View.VISIBLE
    }
}