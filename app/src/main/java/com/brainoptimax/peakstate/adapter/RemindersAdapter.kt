package com.brainoptimax.peakstate.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemRemindersBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.ui.activity.reminders.AddRemindersActivity
import com.squareup.picasso.Picasso
import java.util.*

class RemindersAdapter constructor(private val itemClick: OnItemClickListener) :
    RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {

    var reminderList = mutableListOf<Reminders>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminders, parent, false)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(reminderList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRemindersBinding.bind(view)

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "NewApi")
        fun bindItems(reminder: Reminders) {

            with(binding) {

                when (reminder.title) {
                    "Morning Routine" -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#F4D35E"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_morning)
                    }
                    "Night Routine" -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#19647E"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_night)
                    }
                    "Movement" -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#EE964B"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_movement)
                    }
                    "Fresh Air" -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#28AFB0"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_fresh)
                    }
                    else -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#84AEFF"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_placeholder_image)
                    }
                }

                binding.tvSubtitleRemainder.text = reminder.subtitle
                binding.tvTitleRemainder.text = reminder.title
                binding.tvDescRemainder.text = reminder.description
                binding.tvDatetimeRemainder.text = "${reminder.date} ${reminder.time}"
            }


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AddRemindersActivity::class.java)
                intent.putExtra("data", reminder)
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener {
                itemClick.onItemClick(reminder, adapterPosition)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            reminder: Reminders,
            position: Int
        )
    }
}