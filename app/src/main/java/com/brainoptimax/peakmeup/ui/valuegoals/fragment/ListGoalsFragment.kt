package com.brainoptimax.peakmeup.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.adapter.valuegoals.ValueGoalsAdapter
import com.brainoptimax.peakmeup.model.valuegoals.ValueGoals
import com.brainoptimax.peakmeup.ui.MainActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.quiz.ResultEnergyQuizAdapter
import com.brainoptimax.peakmeup.databinding.FragmentListGoalsBinding
import com.brainoptimax.peakmeup.utils.Preferences


class ListGoalsFragment : Fragment() {

    private var fragmentListGoalsBinding: FragmentListGoalsBinding? = null
    private val binding get() = fragmentListGoalsBinding!!

    private var valueGoalsAdapter: ValueGoalsAdapter? = null
    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var preference: Preferences

    private lateinit var nav : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListGoalsBinding = FragmentListGoalsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = Navigation.findNavController(requireView())

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        binding.backMain.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }

        binding.fabAddValue.setOnClickListener {
            nav.navigate(R.id.action_listGoalsFragment_to_addValueFragment)
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
                    Animatoo.animateSlideUp(requireContext())
                }
            })

        showLoading()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvValue.layoutManager = layoutManager
        valueGoalsAdapter = ValueGoalsAdapter(requireActivity())
        binding.rvValue.adapter = valueGoalsAdapter

        viewModel.allGoals(uidUser!!)
        viewModel.goalsMutableLiveData.observe(viewLifecycleOwner){ valueGoals ->
            Log.d("TAG", "onDataChangeGoals: $valueGoals")
            goneLoading()
            if (valueGoals!!.isEmpty()){
                binding.rvValue.visibility = View.GONE
                binding.layoutEmptyValue.visibility = View.VISIBLE
            }else{
                binding.rvValue.visibility = View.VISIBLE
                binding.layoutEmptyValue.visibility = View.INVISIBLE
            }
            valueGoalsAdapter!!.setGoals(valueGoals)
            valueGoalsAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorGoals.observe(requireActivity()
        ) { error ->
            goneLoading()
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

    }
    private fun showLoading() {
        binding.shimmerValueGoals.startShimmer()
        binding.shimmerValueGoals.visibility = View.VISIBLE
        binding.rvValue.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.shimmerValueGoals.stopShimmer()
        binding.shimmerValueGoals.visibility = View.INVISIBLE
        binding.rvValue.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentListGoalsBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentListGoalsBinding = null
    }


}