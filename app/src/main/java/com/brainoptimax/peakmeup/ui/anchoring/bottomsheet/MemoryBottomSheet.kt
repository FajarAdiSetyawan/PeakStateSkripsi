package com.brainoptimax.peakmeup.ui.anchoring.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.BottomSheetBinding
import com.brainoptimax.peakmeup.ui.anchoring.fragment.Anchoring5Fragment
import com.brainoptimax.peakmeup.utils.Preferences
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MemoryBottomSheet : BottomSheetDialogFragment() {

    private var bottomSheet: BottomSheetBinding? = null
    private val binding get() = bottomSheet!!

    private lateinit var viewModel: AnchoringViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // TODO: memanggil bottomsheetgoals menggunakan viewbinding
        bottomSheet = BottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = 0

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        // Disable Draggable behavior
        behavior.isDraggable = false

        binding.tvGoals.text = resources.getString(R.string.add_memory)

        // TODO: memanggil viewmodel
        viewModel = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

        //TODO: tambah todo list
        binding.ivAdd.setOnClickListener {
            // TODO: mengambil text dari etgoals
            val getEditText = binding.editText.text.toString().trim()
            // TODO: cek text isi/kosong
            if (getEditText.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.memory_empty), Toast.LENGTH_SHORT).show()
            }else{
                // TODO: menambahakan todo list ke firebase menggunakan viewmodel
                viewModel.addMemory(uidUser!!, getEditText)

                viewModel.addMemoryLiveData.observe(viewLifecycleOwner) { status ->
                    if (status.equals("success")){
                        Toast.makeText(requireActivity(), resources.getString(R.string.success_add) + " $getEditText", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireActivity(), status, Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.databaseErrorAddMemory.observe(requireActivity()) { error ->
                    Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                }

                binding.btnSetGoals.visibility = View.VISIBLE

                binding.editText.setText("")
                dialog?.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheet = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheet = null
    }
}