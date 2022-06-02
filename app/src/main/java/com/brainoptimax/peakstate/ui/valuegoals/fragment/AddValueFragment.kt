package com.brainoptimax.peakstate.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.ValueAdapter
import com.brainoptimax.peakstate.databinding.FragmentAddValueBinding


class AddValueFragment : Fragment() {
    private var fragmentAddValueBinding: FragmentAddValueBinding? = null
    private val binding get()  = fragmentAddValueBinding!!

    private var valueAdapter: RecyclerView.Adapter<*>? = null

    private lateinit var nav : NavController

    @SuppressLint("InlinedApi")
    var valueName = arrayOf(
        "Community",
        "Education",
        "Finance",
        "Family",
        "Health",
        "Other",
    )

    // Define an integer array to hold the image recourse ids
    private var valueImages = intArrayOf(
        R.drawable.ic_comunity_value,
        R.drawable.ic_education_value,
        R.drawable.ic_finance_value,
        R.drawable.ic_family_value,
        R.drawable.ic_medic_value,
        R.drawable.ic_other_value,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddValueBinding = FragmentAddValueBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = Navigation.findNavController(requireView())

        binding.rvValue.setHasFixedSize(true)
        // Use a linear layout manager
        val llm = LinearLayoutManager(requireActivity())
        binding.rvValue.layoutManager = llm
        // Create an instance of ProgramAdapter. Pass context and all the array elements to the constructor
        valueAdapter = ValueAdapter(requireActivity(), valueName, valueImages)
        // Finally, attach the adapter with the RecyclerView
        binding.rvValue.adapter = valueAdapter

        binding.backMain.setOnClickListener {
            nav.navigate(R.id.action_addValueFragment_to_listGoalsFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentAddValueBinding=  null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAddValueBinding = null
    }
}