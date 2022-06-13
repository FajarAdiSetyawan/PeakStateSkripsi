package com.brainoptimax.peakmeup.ui.valuegoals.fragment.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditBottomSheetGoals: BottomSheetDialogFragment() {

    private var bottomSheet: BottomSheetBinding? = null
    private val binding get() = bottomSheet!!

    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


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

        // Disable Draggable behavior
        behavior.isDraggable = false
        // TODO: mengambil data yg dikirim dari addgoalsfragment
        val mArgs = arguments
        val myValue = mArgs!!.getString("key")

        // TODO: memanggil viewmodel
        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        // TODO: menginisiasi firebase
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(myValue!!)

        //TODO: tambah todo list
        binding.ivAdd.setOnClickListener {
            // TODO: mengambil text dari etgoals
            val txtToDo = binding.editText.text.toString().trim()
            // TODO: cek text isi/kosong
            if (txtToDo.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.todoblank), Toast.LENGTH_SHORT).show()
            }else{
                // TODO: menambahakan todo list ke firebase menggunakan viewmodel
                val idTodo = databaseReference.child("ToDo").push().key

                viewModel.addToDoList(databaseReference.child("ToDo").child(idTodo!!), view, idTodo, txtToDo, "false")
                Toast.makeText(requireActivity(), resources.getString(R.string.success_add) + " $txtToDo", Toast.LENGTH_SHORT).show()
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