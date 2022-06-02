package com.brainoptimax.peakstate.adapter.valuegoals

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemValueGoalsListBinding
import com.brainoptimax.peakstate.model.valuegoals.ValueGoals
import com.brainoptimax.peakstate.ui.valuegoals.fragment.DetailGoalsFragment
import com.brainoptimax.peakstate.ui.valuegoals.fragment.EditValueGoalsFragment
import com.bumptech.glide.Glide

class ValueGoalsAdapter(var data: List<ValueGoals>?, var context: Context): RecyclerView.Adapter<ValueGoalsAdapter.ViewHolder>()  {

//    private var data: List<ValueGoals>? = null
    fun setGoals(goals: List<ValueGoals>?) {
        this.data = goals
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_value_goals_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    override fun getItemCount(): Int {
        return if (data != null) {
            data!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemValueGoalsListBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(valueGoals: ValueGoals){

            val datetime = valueGoals.date + "/" + valueGoals.time

            binding.tvValue.text = valueGoals.value
            binding.tvDesc.text = valueGoals.descValue
            binding.tvDatetime.text = datetime
            if (valueGoals.img!!.isEmpty()){
                binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
            }else{
                Glide.with(itemView.context)
                    .load(valueGoals.img)
                    .into(binding.ivIconValue)
            }

            itemView.setOnClickListener {
                val fragment: Fragment = DetailGoalsFragment.newInstance(
                    valueGoals.id!!,
                    valueGoals.value!!,
                    valueGoals.statement!!,
                    valueGoals.descValue!!,
                    valueGoals.img!!,
                    datetime
                )
                val fm = (context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayoutVG, fragment)
                ft.commit()
            }

            itemView.setOnLongClickListener {

                val fragment: Fragment = EditValueGoalsFragment.newInstance(
                    valueGoals.id!!,
                    valueGoals.value!!,
                    valueGoals.statement!!,
                    valueGoals.descValue!!,
                    valueGoals.img!!,
                    valueGoals.date!!,
                    valueGoals.time!!,
                )
                val fm = (context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayoutVG, fragment)
                ft.commit()

                true
            }
        }


    }
}

