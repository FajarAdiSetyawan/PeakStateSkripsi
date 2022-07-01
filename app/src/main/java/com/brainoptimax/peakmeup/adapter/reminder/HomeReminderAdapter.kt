package com.brainoptimax.peakmeup.adapter.reminder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.Reminders
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemRemindersHomeBinding
import com.brainoptimax.peakmeup.model.Reminder
import com.brainoptimax.peakmeup.ui.reminders.fragment.DetailReminderFragment
import com.brainoptimax.peakmeup.ui.reminders.fragment.EditReminderFragment
import com.bumptech.glide.Glide
import java.util.*

class HomeReminderAdapter(var context: Context) : RecyclerView.Adapter<HomeReminderAdapter.ViewHolder>() {
    var reminderList: List<Reminder>? = null

    fun setReminder(reminder: List<Reminder>?) {
        this.reminderList = reminder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminders_home, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminderList!![position])
    }

    override fun getItemCount(): Int {
        return if (reminderList != null) {
            reminderList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRemindersHomeBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(reminder: Reminder) {
            if (reminder.imgurl!!.isEmpty()){
                when (reminder.title) {
                    itemView.context.resources.getString(R.string.morning_routine) -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#F4D35E"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_morning)
                    }
                    itemView.context.resources.getString(R.string.night_routine) -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#19647E"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_night)
                    }
                    itemView.context.resources.getString(R.string.movement) -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#EE964B"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_movement)
                    }
                    itemView.context.resources.getString(R.string.fresh_air) -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#28AFB0"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_fresh)
                    }
                    else -> {
                        binding.tvTitleRemainder.setTextColor(Color.parseColor("#84AEFF"))
                        binding.ivMoreRemainder.setImageResource(R.drawable.ic_placeholder_image)
                    }
                }
            }else{
                Glide.with(itemView.context)
                    .load(reminder.imgurl)
                    .into(binding.ivMoreRemainder)
            }

            val datetime = "${reminder.date} ${reminder.time}"

            binding.tvSubtitleRemainder.text = reminder.subtitle
            binding.tvTitleRemainder.text = reminder.title
            binding.tvDescRemainder.text = reminder.description
            binding.tvDatetimeRemainder.text = datetime


            itemView.setOnClickListener {
                val fragment: Fragment = DetailReminderFragment.newInstance(
                    reminder.idReminder!!,
                    reminder.title!!,
                    reminder.subtitle!!,
                    reminder.description!!,
                    datetime,
                    reminder.imgurl!!
                )
                val fm = (context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayoutReminder, fragment)
                ft.commit()
                ft.addToBackStack(null)
            }

            itemView.setOnLongClickListener {
                val fragment: Fragment = EditReminderFragment.newInstance(
                    reminder.idReminder!!,
                    reminder.title!!,
                    reminder.subtitle!!,
                    reminder.description!!,
                    reminder.date!!,
                    reminder.time!!,
                    reminder.imgurl!!
                )
                val fm = (context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.frameLayoutReminder, fragment)
                ft.commit()
                ft.addToBackStack(null)


                true
            }
        }

    }
}