package com.brainoptimax.peakstate.adapter.valuegoals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemAddGoalsBinding
import com.brainoptimax.peakstate.model.Goals

class GoalsAdapter(private val goalsList: ArrayList<Goals>, var context: Context) :
    RecyclerView.Adapter<GoalsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_goals, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goalsList[position])
    }

    override fun getItemCount(): Int = goalsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemAddGoalsBinding.bind(view)

        fun bind(goals: Goals) {
            with(binding) {
                tvListGoals.text = goals.goals
            }

        }
    }
}