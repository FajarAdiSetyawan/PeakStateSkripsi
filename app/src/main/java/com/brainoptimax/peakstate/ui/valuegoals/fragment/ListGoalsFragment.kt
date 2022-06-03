package com.brainoptimax.peakstate.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.ValueGoalsAdapter
import com.brainoptimax.peakstate.databinding.FragmentListGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ValueGoals
import com.brainoptimax.peakstate.ui.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.valuegoals.ValueGoalsViewModel


class ListGoalsFragment : Fragment() {

    private var fragmentListGoalsBinding: FragmentListGoalsBinding? = null
    private val binding get() = fragmentListGoalsBinding!!

    private lateinit var valueGoals: ArrayList<ValueGoals>
    private var valueGoalsAdapter: ValueGoalsAdapter? = null
    private lateinit var viewModel: ValueGoalsViewModel

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

        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvValue.layoutManager = layoutManager

        binding.backMain.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }

        binding.fabAddValue.setOnClickListener {
            nav.navigate(R.id.action_listGoalsFragment_to_addValueFragment)
        }

        binding.rvValue.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvValue.layoutManager = linearLayoutManager

        showLoading()
        viewModel.allData
        viewModel.goalsMutableLiveData.observe(requireActivity()){ valueGoals ->
            Log.d("TAG", "onDataChangeGoals: $valueGoals")
            if (valueGoals!!.isEmpty()){
                goneLoading()
                binding.layoutEmptyValue.visibility = View.VISIBLE
                binding.rvValue.visibility = View.INVISIBLE
            }else{
                goneLoading()
                binding.layoutEmptyValue.visibility = View.INVISIBLE
                valueGoalsAdapter = ValueGoalsAdapter(valueGoals, requireActivity())
                binding.rvValue.adapter = valueGoalsAdapter
                valueGoalsAdapter!!.notifyDataSetChanged()
                binding.rvValue.visibility = View.VISIBLE
            }
        }

        viewModel.databaseErrorGoals.observe(requireActivity()
        ) { error ->
            goneLoading()
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
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