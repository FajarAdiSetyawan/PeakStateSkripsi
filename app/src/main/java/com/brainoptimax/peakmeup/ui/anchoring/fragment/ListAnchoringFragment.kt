package com.brainoptimax.peakmeup.ui.anchoring.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.anchoring.AnchoringAdapter
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.brainoptimax.peakmeup.databinding.FragmentListAnchoringBinding
import com.brainoptimax.peakmeup.ui.MainActivity
import com.brainoptimax.peakmeup.ui.anchoring.AnchoringActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences

class ListAnchoringFragment : Fragment() {
    private var fragmentListAnchoringBinding: FragmentListAnchoringBinding? = null
    private val binding get() = fragmentListAnchoringBinding!!

    private var anchoringAdapter: AnchoringAdapter? = null
    private lateinit var viewModel: AnchoringViewModel

    private lateinit var preference: Preferences

    private lateinit var nav : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListAnchoringBinding = FragmentListAnchoringBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = Navigation.findNavController(requireView())
        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvAnchoring.layoutManager = layoutManager
        anchoringAdapter = AnchoringAdapter(requireActivity())
        binding.rvAnchoring.adapter = anchoringAdapter

        showLoading()
        viewModel.allAnchoring(uidUser!!)
        viewModel.anchoringMutableLiveData.observe(viewLifecycleOwner) { anchoring ->
            hideLoading()
            if (anchoring!!.isEmpty()){
                binding.rvAnchoring.visibility = View.GONE
                binding.layoutEmptyValue.visibility = View.VISIBLE
            }else{
                binding.rvAnchoring.visibility = View.VISIBLE
                binding.layoutEmptyValue.visibility = View.INVISIBLE
            }
            anchoringAdapter!!.setAnchoring(anchoring)
            anchoringAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorAnchoring.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.fabAddAnchoring.setOnClickListener {
            nav.navigate(R.id.action_listAnchoringFragment_to_anchoring1Fragment)
        }

        binding.backMain.setOnClickListener {
            startActivity(Intent(requireActivity(), AnchoringActivity::class.java)) // pindah ke login
            Animatoo.animateSlideRight(requireContext())
        }


    }

    private fun showLoading(){
        binding.rvAnchoring.visibility = View.INVISIBLE
        binding.shimmerAnchoring.visibility = View.VISIBLE
        binding.shimmerAnchoring.startShimmer()
    }

    private fun hideLoading(){
        binding.rvAnchoring.visibility = View.VISIBLE
        binding.shimmerAnchoring.visibility = View.INVISIBLE
        binding.shimmerAnchoring.stopShimmer()
    }
}