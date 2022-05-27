package com.brainoptimax.peakstate.adapter.reminder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemRemindersBinding
import com.brainoptimax.peakstate.model.Reminders
import java.util.*

class RemindersAdapter(
    private val reminderList: MutableList<Reminders>,
    var context: Context,
    var clickListener: ReminderClickListener
) : RecyclerView.Adapter<RemindersAdapter.ViewHolder>(), Filterable {

    var allReminder: List<Reminders> = ArrayList(reminderList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminders, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    override fun getItemCount(): Int = reminderList.size

    fun getReminderAt(position: Int): Reminders {
        return reminderList.get(position)
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filteredList: MutableList<Reminders> = ArrayList()
            if (charSequence == null || charSequence.isEmpty()) {
                filteredList.addAll(allReminder)
            } else {
                val filteredPattern =
                    charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in allReminder) {
                    if (item.title.lowercase(Locale.getDefault()).contains(filteredPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            reminderList.clear()
            reminderList.addAll(filterResults!!.values as MutableList<Reminders>)
            notifyDataSetChanged()
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRemindersBinding.bind(view)

        fun bind(reminders: Reminders) {
            when (reminders.title) {
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