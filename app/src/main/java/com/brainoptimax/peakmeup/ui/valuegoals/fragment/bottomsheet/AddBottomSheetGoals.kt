package com.brainoptimax.peakmeup.ui.valuegoals.fragment.bottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakmeup.adapter.valuegoals.AddToDoAdapter
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.BottomSheetGoalsBinding
import com.brainoptimax.peakmeup.utils.Preferences
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddBottomSheetGoals: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetGoalsBinding
    private lateinit var viewModel: ValueGoalsViewModel

//    private lateinit var auth: FirebaseAuth
//    private lateinit var databaseReference: DatabaseReference

    private var toDoAdapter: AddToDoAdapter? = null

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // TODO: memanggil bottomsheetgoals menggunakan viewbinding
        binding = BottomSheetGoalsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

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
//        auth = FirebaseAuth.getInstance()
//        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(myValue!!)

        //TODO: tambah todo list
        binding.ivAdd.setOnClickListener {
            // TODO: mengambil text dari etgoals
            val txtToDo = binding.etGoals.text.toString().trim()
            // TODO: cek text isi/kosong
            if (txtToDo.isEmpty()){
                Toast.makeText(requireActivity(), resources.getString(R.string.todoblank), Toast.LENGTH_SHORT).show()
            }else{
                // TODO: menambahakan todo list ke firebase menggunakan viewmodel
//                val idTodo = databaseReference.child("ToDo").push().key
//
//                viewModel.addToDoList(databaseReference.child("ToDo").child(idTodo!!), view, idTodo, txtToDo, "false")
                viewModel.addToDoList(uidUser!!, myValue!!, txtToDo)
                viewModel.addTodoMutableLiveData.observe(requireActivity()) { status ->
                    if(status.equals("success")){
                        binding.btnSetGoals.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), resources.getString(R.string.success_add) + " $txtToDo", Toast.LENGTH_SHORT).show()
                        binding.etGoals.setText("")
                    }
                }

                viewModel.databaseErrorAddTodo.observe(requireActivity()
                ) { error ->
                    Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.rvGoals.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvGoals.layoutManager = linearLayoutManager
        toDoAdapter = AddToDoAdapter()
        binding.rvGoals.adapter = toDoAdapter

        viewModel.allToDo(uidUser!!, myValue!!)
        viewModel.todoMutableLiveData.observe(requireActivity()){ toDo ->
            toDoAdapter!!.setTodo(toDo)
            toDoAdapter!!.notifyDataSetChanged()

        }

        viewModel.databaseErrorToDo.observe(requireActivity()
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        // TODO: klik tombol
        binding.btnSetGoals.setOnClickListener {
            startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }


        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            @Suppress("DEPRECATED_IDENTITY_EQUALS")
            override fun onKey(p0: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle(resources.getString(R.string.exitaddgoals))
                            .setMessage(resources.getString(R.string.msg_exitgoal))
                            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                                // Respond to neutral button press
                            }
                            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
//                                databaseReference.removeValue()
                                viewModel.removeGoal(uidUser, myValue)
                                viewModel.deleteGoalsMutableLiveData.observe(requireActivity()) { success ->
                                    if(success.equals("success")){
                                        startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                                        Animatoo.animateSlideUp(requireContext())
                                    }
                                }

                                viewModel.databaseErrorDeleteGoals.observe(requireActivity()
                                ) { error ->
                                    Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
                            .show()

                        return true
                    }
                }
                return false
            }
        })

    }


}