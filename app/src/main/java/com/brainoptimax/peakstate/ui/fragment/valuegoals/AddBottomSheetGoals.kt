package com.brainoptimax.peakstate.ui.fragment.valuegoals

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
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.AddToDoAdapter
import com.brainoptimax.peakstate.adapter.valuegoals.DetailValueGoalsAdapter
import com.brainoptimax.peakstate.databinding.BottomSheetGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.brainoptimax.peakstate.ui.activity.goals.ValueGoalsActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.valuegoals.ValueGoalsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AddBottomSheetGoals: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetGoalsBinding
    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var goals: MutableList<ToDo>
    private var toDoAdapter: AddToDoAdapter? = null

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
            }
        }

        binding.rvGoals.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvGoals.layoutManager = linearLayoutManager
        toDoAdapter = AddToDoAdapter()
        binding.rvGoals.adapter = toDoAdapter

        viewModel.allToDo(myValue)
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
                                databaseReference.removeValue()
                                startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                                Animatoo.animateSlideUp(requireContext())
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