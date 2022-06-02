package com.brainoptimax.peakstate.ui.anchoring.fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.databinding.FragmentAnchoring6Binding
import com.brainoptimax.peakstate.ui.anchoring.AnchoringActivity
import com.brainoptimax.peakstate.utils.Animatoo


class Anchoring6Fragment : Fragment() {

    private var fragmentAnchoring6Binding: FragmentAnchoring6Binding? = null
    private val binding get() = fragmentAnchoring6Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring6Binding = FragmentAnchoring6Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true
                    }
                }
                return false
            }
        })

        binding.btnDone.setOnClickListener {
            startActivity(Intent(requireActivity(), AnchoringActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }
    }

}