package com.brainoptimax.peakmeup.adapter.valuegoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemEditGoalsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditGoalsAdapter(private val goalId: String?) :
    RecyclerView.Adapter<EditGoalsAdapter.ViewHolder>() {

    private var idGoals: String? = null

    private var toDoList: List<ToDo>? = null

    private var onItemClickListener:((ToDo)->Unit)? = null

    fun setOnItemClickListener(listener: (ToDo)->Unit) {
        onItemClickListener = listener
    }

    fun setTodo(todo: List<ToDo>?) {
        this.toDoList = todo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        idGoals = goalId

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

            binding.tvListGoals.text = toDo.todo

            binding.ivRemoveGoals.setOnClickListener {
                onItemClickListener?.let{
                    it(toDo)
                }
            }
        }
    }

}
