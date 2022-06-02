package com.brainoptimax.peakstate.adapter.valuegoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemDetailValueGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DetailValueGoalsAdapter(private val goalsID: String?) :
    RecyclerView.Adapter<DetailValueGoalsAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var idGoals: String? = null

    private var toDoList: List<ToDo>? = null

    fun setTodo(todo: List<ToDo>?) {
        this.toDoList = todo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        auth = FirebaseAuth.getInstance()
        idGoals = goalsID
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals").child(idGoals!!).child("ToDo")

        val inflate =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_value_goals, parent, false)
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
        private val binding = ItemDetailValueGoalsBinding.bind(view)
        fun bind(toDo: ToDo) {

            with(binding) {
                cbGoalsList.text = toDo.goals
                val checked = toDo.isCompleted.toString()
                cbGoalsList.isChecked = checked == "true"

                val idTodo = toDo.id
                val dbTodo = databaseReference.child(idTodo!!).child("completed")

                cbGoalsList.setOnCheckedChangeListener { _, _ ->
                    if (cbGoalsList.isChecked) {
                        dbTodo.setValue("true").addOnSuccessListener {
                                Toast.makeText(itemView.context, "Done", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        dbTodo.setValue("false").addOnSuccessListener {
                                Toast.makeText(itemView.context, "Not Done", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
        }

    }
}