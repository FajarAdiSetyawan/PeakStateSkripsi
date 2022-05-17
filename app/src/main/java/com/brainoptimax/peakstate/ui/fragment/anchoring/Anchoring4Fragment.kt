package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAnchoring4Binding


class Anchoring4Fragment : Fragment() {

    private var fragmentAnchoring4Binding: FragmentAnchoring4Binding? = null
    private val binding get() = fragmentAnchoring4Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring4Binding = FragmentAnchoring4Binding.inflate(layoutInflater, container, false)
        return binding.root
    }


}