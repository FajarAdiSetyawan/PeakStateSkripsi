package com.brainoptimax.peakmeup.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.Reminders
import com.brainoptimax.peakmeup.ui.reminders.DetailReminderActivity
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemRemindersHomeBinding

class HomeRemindersAdapter: RecyclerView.Adapter<HomeRemindersAdapter.ViewHolder>() {

    private var reminderList = mutableListOf<Reminders>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminders_home, parent, false)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(reminderList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRemindersHomeBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n", "NewApi")
        fun bindItems(reminder: Reminders) {
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


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailReminderActivity::class.java)
                intent.putExtra(DetailReminderActivity.EXTRA_TITLE, reminder.title)
                intent.putExtra(DetailReminderActivity.EXTRA_SUBTITLE, reminder.subtitle)
                intent.putExtra(DetailReminderActivity.EXTRA_DESC, reminder.description)
                intent.putExtra(DetailReminderActivity.EXTRA_DATE, reminder.date)
                intent.putExtra(DetailReminderActivity.EXTRA_TIME, reminder.time)
                itemView.context.startActivity(intent)
            }



        }
    }


}