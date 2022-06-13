package com.brainoptimax.peakmeup.ui.breathing

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.CustomBreathingLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBreathBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: CustomBreathingLayoutBinding
    private var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomBreathingLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

        binding.breathInSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.breathIn0.text = "" + binding.breathInSeekBar.progress
                mListener!!.onInhaleProgressChanged(binding.breathInSeekBar.progress * 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.breathOutSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.breathOut0.text = "" + binding.breathOutSeekBar.progress
                mListener!!.onExhaleProgressChanged(binding.breathOutSeekBar.progress * 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.holdSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                binding.hold0.text = "" + binding.holdSeekBar.progress
                mListener!!.onHoldProgressChanged(binding.holdSeekBar.progress * 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.resetBtn.setOnClickListener {
            mListener!!.onButtonClicked(4000, 2000, 4000)
            dismiss()
        }
    }

    interface BottomSheetListener {
        fun onButtonClicked(inhale: Int, exhale: Int, hold: Int)
        fun onInhaleProgressChanged(inhale: Int)
        fun onExhaleProgressChanged(exhale: Int)
        fun onHoldProgressChanged(hold: Int)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as BottomSheetListener
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}