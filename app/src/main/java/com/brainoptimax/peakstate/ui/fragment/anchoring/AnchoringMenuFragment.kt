package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAnchoringMenuBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroAnchoringActivity
import com.brainoptimax.peakstate.utils.Animatoo

class AnchoringMenuFragment : Fragment() {
    private var fragmentAnchoringMenuBinding: FragmentAnchoringMenuBinding? = null
    private val binding get() = fragmentAnchoringMenuBinding!!

    private lateinit var nav : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoringMenuBinding = FragmentAnchoringMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = Navigation.findNavController(requireView())

        binding.btnStart.setOnClickListener {
            nav.navigate(R.id.action_anchoringMenuFragment_to_anchoring1Fragment)
        }

        binding.ivInfo.setOnClickListener {
            startActivity(Intent(requireActivity(), IntroAnchoringActivity::class.java)) // pindah ke login
            Animatoo.animateSlideRight(requireContext())
        }

        binding.btnLast.setOnClickListener {
            nav.navigate(R.id.action_anchoringMenuFragment_to_listAnchoringFragment)
        }

        binding.ivBack.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java)) // pindah ke login
            Animatoo.animateSlideRight(requireContext())
        }

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(requireActivity(), MainActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideRight(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }


}