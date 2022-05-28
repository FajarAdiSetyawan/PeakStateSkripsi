package com.brainoptimax.peakstate.adapter.valuegoals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemAddGoalsBinding
import com.brainoptimax.peakstate.model.valuegoals.ToDo

class AddToDoAdapter :
    RecyclerView.Adapter<AddToDoAdapter.ViewHolder>() {

    private var toDoList: List<ToDo>? = null

    fun setTodo(todo: List<ToDo>?) {
        this.toDoList = todo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_goals, parent, false)
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
        private val binding = ItemAddGoalsBinding.bind(view)

        fun bind(toDo: ToDo) {
            with(binding) {
                tvListGoals.text = toDo.goals
            }

        }
    }
}