package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAnchoring3Binding


class Anchoring3Fragment : Fragment() {

    private var fragmentAnchoring3Binding: FragmentAnchoring3Binding? = null
    private val binding get() = fragmentAnchoring3Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring3Binding = FragmentAnchoring3Binding.inflate(layoutInflater, container, false)
        return binding.root
    }


}