package com.brainoptimax.peakstate.adapter.reminder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemRemindersHomeBinding
import com.brainoptimax.peakstate.model.Reminders
import java.util.*

class HomeReminderAdapter(
    private val reminderList: MutableList<Reminders>,
    var context: Context,
    var clickListener: ReminderClickListener
) : RecyclerView.Adapter<HomeReminderAdapter.ViewHolder>() {

    var allReminder: List<Reminders> = ArrayList(reminderList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminders_home, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    override fun getItemCount(): Int = reminderList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRemindersHomeBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(reminders: Reminders) {
            when (reminders.title) {
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

            binding.tvSubtitleRemainder.text = reminders.subtitle
            binding.tvTitleRemainder.text = reminders.title
            binding.tvDescRemainder.text = reminders.description
            binding.tvDatetimeRemainder.text = "${reminders.date} ${reminders.time}"

            itemView.setOnClickListener {
                clickListener.click(reminders)
            }

            itemView.setOnLongClickListener {
                clickListener.OnLongClick(reminders)
                false
            }
        }

    }
}