package com.brainoptimax.peakmeup.ui.anchoring.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.anchoring.MemoryAdapter
import com.brainoptimax.peakmeup.databinding.FragmentAnchoring3Binding
import com.brainoptimax.peakmeup.ui.anchoring.bottomsheet.MemoryBottomSheet
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class Anchoring3Fragment : Fragment() {

    private var fragmentAnchoring3Binding: FragmentAnchoring3Binding? = null
    private val binding get() = fragmentAnchoring3Binding!!

    private var memoryAdapter: MemoryAdapter? = null
    private lateinit var viewModel: AnchoringViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring3Binding = FragmentAnchoring3Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mBundle: Bundle? = arguments
        val resourceful =  mBundle!!.getString("resourceful")

        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvMemory.layoutManager = layoutManager
        memoryAdapter = MemoryAdapter()
        binding.rvMemory.adapter = memoryAdapter

        binding.progressBar.visibility = View.VISIBLE
        viewModel.allMemory
        viewModel.memoryMutableLiveData.observe(requireActivity()) { memory ->
            binding.progressBar.visibility = View.GONE

            memoryAdapter!!.setMemory(memory)
            memoryAdapter!!.notifyDataSetChanged()
        }
        viewModel.databaseErrorMemory.observe(
            requireActivity()
        ) { error ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        memoryAdapter!!.setOnItemClickListener { memory ->
            val fragment = Anchoring4Fragment() // replace your custom fragment class
            val bundle = Bundle()
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            bundle.putString("resourceful", resourceful) // use as per your need
            bundle.putString("memory", memory.memory) // use as per your need

            fragment.arguments = bundle
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.frameLayoutAnchoring, fragment)
            fragmentTransaction.commit()
        }


        memoryAdapter!!.setOnItemDeleteClickListener { memory ->
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) + " ${memory.memory} ?")
                .setPositiveButton("Ok") { _, _ ->

                    viewModel.deleteMemory(memory.id!!)
                    Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " ${memory.memory}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, which -> }
                .show()
        }

        binding.fabAddMemory.setOnClickListener {
            val newFragment: BottomSheetDialogFragment = MemoryBottomSheet()
            newFragment.show(requireActivity().supportFragmentManager, "TAG")
        }
    }

}