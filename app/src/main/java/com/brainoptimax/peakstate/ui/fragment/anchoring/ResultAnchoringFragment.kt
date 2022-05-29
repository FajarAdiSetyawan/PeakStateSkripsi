package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentResultAnchoringBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val RESOURCEFUL = "resourceful"
private const val MEMORY = "memory"
private const val DESC = "desc"
private const val DATETIME = "datetime"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultAnchoringFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultAnchoringFragment : Fragment() {

    private var fragmentResultAnchoringBinding: FragmentResultAnchoringBinding? = null
    private val binding get() = fragmentResultAnchoringBinding!!

    // TODO: Rename and change types of parameters
    private var resourceful: String? = null
    private var memory: String? = null
    private var desc: String? = null
    private var datetime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resourceful = it.getString(RESOURCEFUL)
            memory = it.getString(MEMORY)
            desc = it.getString(DESC)
            datetime = it.getString(DATETIME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultAnchoringBinding = FragmentResultAnchoringBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultAnchoringFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(resourceful: String, memory: String, desc: String, datetime: String) =
            ResultAnchoringFragment().apply {
                arguments = Bundle().apply {
                    putString(RESOURCEFUL, resourceful)
                    putString(MEMORY, memory)
                    putString(DESC, desc)
                    putString(DATETIME, datetime)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDateTime.text = datetime
        binding.tvResult.text = resourceful
        binding.tvMemory.text = memory
        binding.tvDescResultQuiz.text = desc

        binding.btnDone.setOnClickListener {

        }
    }
}