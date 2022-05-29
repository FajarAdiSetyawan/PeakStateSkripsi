package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.anchoring.AnchoringAdapter
import com.brainoptimax.peakstate.adapter.anchoring.ResourcefulAdapter
import com.brainoptimax.peakstate.databinding.FragmentListAnchoringBinding
import com.brainoptimax.peakstate.viewmodel.anchoring.AnchoringViewModel

class ListAnchoringFragment : Fragment() {
    private var fragmentListAnchoringBinding: FragmentListAnchoringBinding? = null
    private val binding get() = fragmentListAnchoringBinding!!

    private var anchoringAdapter: AnchoringAdapter? = null
    private lateinit var viewModel: AnchoringViewModel

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

        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvAnchoring.layoutManager = layoutManager
        anchoringAdapter = AnchoringAdapter(requireActivity())
        binding.rvAnchoring.adapter = anchoringAdapter

        showLoading()
        viewModel.allAnchoring
        viewModel.anchroingMutableLiveData.observe(requireActivity()) { anchoring ->
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
        viewModel.databaseErrorAnchroing.observe(
            requireActivity()
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
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