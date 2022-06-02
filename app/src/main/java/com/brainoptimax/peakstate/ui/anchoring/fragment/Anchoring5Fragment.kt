package com.brainoptimax.peakstate.ui.anchoring.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentAnchoring5Binding
import render.animations.Render
import render.animations.Slide


class Anchoring5Fragment : Fragment() {

    private var fragmentAnchoring5Binding: FragmentAnchoring5Binding? = null
    private val binding get() = fragmentAnchoring5Binding!!

    private lateinit var nav : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring5Binding = FragmentAnchoring5Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = Render(requireActivity())


        nav = Navigation.findNavController(requireView())

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

        binding.btnStart.setOnClickListener {
            animation.setAnimation(Slide.OutDown(binding.btnStart))
            animation.start()
            binding.btnStart.visibility = View.INVISIBLE

            object : CountDownTimer(15000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvTimer.text = (millisUntilFinished / 1000 + 1).toString() + "  " + resources.getString(
                        R.string.seconds)
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    binding.tvTimer.text = "0  " + resources.getString(R.string.seconds)
                    binding.btnDone.visibility = View.VISIBLE
                    animation.setAnimation(Slide.InUp(binding.btnDone))
                    animation.start()
                }
            }.start()
        }

        binding.btnDone.setOnClickListener {
            val fragment = Anchoring6Fragment() // replace your custom fragment class
            val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.frameLayoutAnchoring, fragment)
            fragmentTransaction.commit()
        }
    }


}