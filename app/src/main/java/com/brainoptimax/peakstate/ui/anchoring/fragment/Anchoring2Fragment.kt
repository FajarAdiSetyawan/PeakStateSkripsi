package com.brainoptimax.peakstate.ui.anchoring.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.brainoptimax.peakstate.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mBundle: Bundle? = arguments
        val resourceful =  mBundle!!.getString("resourceful")

        binding.btnNext.setOnClickListener {
            val fragment = Anchoring3Fragment() // replace your custom fragment class
            val bundle = Bundle()
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            bundle.putString("resourceful", resourceful)
            fragment.arguments = bundle
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.frameLayoutAnchoring, fragment)
            fragmentTransaction.commit()
        }
    }
}