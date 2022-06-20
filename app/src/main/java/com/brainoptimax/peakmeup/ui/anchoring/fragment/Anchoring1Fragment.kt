package com.brainoptimax.peakmeup.ui.anchoring.fragment

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
import com.brainoptimax.peakmeup.adapter.anchoring.ResourcefulAdapter
import com.brainoptimax.peakmeup.ui.anchoring.bottomsheet.ResourcefulBottomSheet
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.emotions.DailyEmotionAdapter
import com.brainoptimax.peakmeup.databinding.FragmentAnchoring1Binding
import com.brainoptimax.peakmeup.utils.Preferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Anchoring1Fragment : Fragment() {

    private var fragmentAnchoring1Binding: FragmentAnchoring1Binding? = null
    private val binding get() = fragmentAnchoring1Binding!!

    private var resourcefulAdapter: ResourcefulAdapter? = null
    private lateinit var viewModel: AnchoringViewModel

    private lateinit var preference: Preferences

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

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvResourceful.layoutManager = layoutManager
        resourcefulAdapter = ResourcefulAdapter()
        binding.rvResourceful.adapter = resourcefulAdapter

        binding.progressBar.visibility = View.VISIBLE
        viewModel.allResourceful(uidUser!!)
        viewModel.resourcefulMutableLiveData.observe(viewLifecycleOwner) { resourceful ->
            binding.progressBar.visibility = View.GONE
            if (resourceful!!.isEmpty()){
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvResourceful.visibility = View.INVISIBLE
            }else{
                binding.layoutEmpty.visibility = View.INVISIBLE
                resourcefulAdapter!!.setResourceful(resourceful)
                resourcefulAdapter!!.notifyDataSetChanged()
                binding.rvResourceful.visibility = View.VISIBLE
            }
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

                    viewModel.deleteResourceful(uidUser, resourceful.idResourceful!!)
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