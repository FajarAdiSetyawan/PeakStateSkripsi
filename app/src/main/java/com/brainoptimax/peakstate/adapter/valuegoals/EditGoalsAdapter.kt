package com.brainoptimax.peakstate.adapter.valuegoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemEditGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditGoalsAdapter(private val goalId: String?) :
    RecyclerView.Adapter<EditGoalsAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var idGoals: String? = null

    private var toDoList: List<ToDo>? = null

    fun setTodo(todo: List<ToDo>?) {
        this.toDoList = todo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        idGoals = goalId
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(idGoals!!).child("ToDo")

        val inflate =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_edit_goals, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(toDoList!![position])
    }

    override fun getItemCount(): Int {
        return if (toDoList != null) {
            toDoList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemEditGoalsBinding.bind(view)
        fun bind(toDo: ToDo) {

            binding.tvListGoals.text = toDo.goals

            binding.ivRemoveGoals.setOnClickListener {
                MaterialAlertDialogBuilder(itemView.context, R.style.MaterialAlertDialogRounded)
                    .setTitle("Confirm the action")
                    .setMessage("Are you sure you delete Todo ${toDo.goals} ?")
                    .setPositiveButton("Ok") { _, _ ->
                        databaseReference.child(toDo.id!!).removeValue().addOnSuccessListener {
                            Toast.makeText(itemView.context, "Success Delete", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, which -> }
                    .show()


            }
        }
    }

}
