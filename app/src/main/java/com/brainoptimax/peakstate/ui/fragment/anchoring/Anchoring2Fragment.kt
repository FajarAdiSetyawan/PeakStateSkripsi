package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.databinding.FragmentAnchoring2Binding


class Anchoring2Fragment : Fragment() {

    private var fragmentAnchoring2Binding: FragmentAnchoring2Binding? = null
    private val binding get() = fragmentAnchoring2Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring2Binding = FragmentAnchoring2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

}