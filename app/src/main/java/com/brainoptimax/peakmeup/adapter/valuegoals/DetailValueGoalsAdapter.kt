package com.brainoptimax.peakmeup.adapter.valuegoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemDetailValueGoalsBinding
import com.brainoptimax.peakmeup.model.anchoring.Resourceful
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DetailValueGoalsAdapter(private val goalsID: String?) :
    RecyclerView.Adapter<DetailValueGoalsAdapter.ViewHolder>() {
    private var toDoList: List<ToDo>? = null

    private var onItemDoneClickListener:((ToDo)->Unit)? = null
    private var onItemNotDoneClickListener:((ToDo)->Unit)? = null

    private var idGoals: String? = null

    fun setOnItemDoneClickListener(listener: (ToDo)->Unit) {
        onItemDoneClickListener = listener
    }

    fun setOnItemNotDoneClickListener(listener: (ToDo)->Unit) {
        onItemNotDoneClickListener = listener
    }

    fun setTodo(todo: List<ToDo>?) {
        this.toDoList = todo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        idGoals = goalsID

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
                cbGoalsList.text = toDo.todo
                val checked = toDo.isCompleted.toString()
                cbGoalsList.isChecked = checked == "true"

                cbGoalsList.setOnCheckedChangeListener { _, _ ->
                    if (cbGoalsList.isChecked) {
                        onItemDoneClickListener?.let{
                            it(toDo)
                        }
                    } else {
                        onItemNotDoneClickListener?.let{
                            it(toDo)
                        }
                    }
                }
            }
        }
    }
}