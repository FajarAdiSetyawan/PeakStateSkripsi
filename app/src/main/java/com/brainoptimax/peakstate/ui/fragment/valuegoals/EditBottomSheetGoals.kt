package com.brainoptimax.peakstate.ui.fragment.valuegoals

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.EditTodoBottomSheetBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.brainoptimax.peakstate.ui.activity.goals.ValueGoalsActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.valuegoals.ValueGoalsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class EditBottomSheetGoals: BottomSheetDialogFragment() {

    private var editTodoBottomSheetBinding: EditTodoBottomSheetBinding? = null
    private val binding get() = editTodoBottomSheetBinding!!

    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var goals: MutableList<ToDo>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // TODO: memanggil bottomsheetgoals menggunakan viewbinding
        editTodoBottomSheetBinding = EditTodoBottomSheetBinding.inflate(layoutInflater, container, false)
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
            val txtToDo = binding.etGoals.text.toString().trim()
            // TODO: cek text isi/kosong
            if (txtToDo.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.todoblank), Toast.LENGTH_SHORT).show()
            }else{
                // TODO: menambahakan todo list ke firebase menggunakan viewmodel
                val idTodo = databaseReference.child("ToDo").push().key

                viewModel.addToDoList(databaseReference.child("ToDo").child(idTodo!!), view, idTodo, txtToDo, "false")

                binding.btnSetGoals.visibility = View.VISIBLE

                binding.etGoals.setText("")
                dialog?.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTodoBottomSheetBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editTodoBottomSheetBinding = null
    }

}