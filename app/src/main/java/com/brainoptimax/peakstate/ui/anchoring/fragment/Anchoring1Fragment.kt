package com.brainoptimax.peakstate.ui.anchoring.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.anchoring.ResourcefulAdapter
import com.brainoptimax.peakstate.databinding.FragmentAnchoring1Binding
import com.brainoptimax.peakstate.ui.anchoring.bottomsheet.ResourcefulBottomSheet
import com.brainoptimax.peakstate.viewmodel.anchoring.AnchoringViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Anchoring1Fragment : Fragment() {

    private var fragmentAnchoring1Binding: FragmentAnchoring1Binding? = null
    private val binding get() = fragmentAnchoring1Binding!!

    private var resourcefulAdapter: ResourcefulAdapter? = null
    private lateinit var viewModel: AnchoringViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring1Binding = FragmentAnchoring1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvResourceful.layoutManager = layoutManager
        resourcefulAdapter = ResourcefulAdapter()
        binding.rvResourceful.adapter = resourcefulAdapter

        binding.progressBar.visibility = View.VISIBLE
        viewModel.allResourceful
        viewModel.resourcefulMutableLiveData.observe(requireActivity()) { toDo ->
            binding.progressBar.visibility = View.GONE
            resourcefulAdapter!!.setResourceful(toDo)
            resourcefulAdapter!!.notifyDataSetChanged()
        }
        viewModel.databaseErrorResourceful.observe(
            requireActivity()
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        resourcefulAdapter!!.setOnItemClickListener { resourceful ->
            val fragment = Anchoring2Fragment() // replace your custom fragment class
            val bundle = Bundle()
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            bundle.putString("resourceful", resourceful.resourceful) // use as per your need

            fragment.arguments = bundle
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.frameLayoutAnchoring, fragment)
            fragmentTransaction.commit()
        }


        resourcefulAdapter!!.setOnItemDeleteClickListener { resourceful ->
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) + " ${resourceful.resourceful} ?")
                .setPositiveButton("Ok") { _, _ ->

                    viewModel.deleteResourceful(resourceful.id!!)
                    Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " ${resourceful.resourceful}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, which -> }
                .show()
        }

        binding.fabAddResourceful.setOnClickListener {
            val newFragment: BottomSheetDialogFragment = ResourcefulBottomSheet()
            newFragment.show(requireActivity().supportFragmentManager, "TAG")
        }
    }

}