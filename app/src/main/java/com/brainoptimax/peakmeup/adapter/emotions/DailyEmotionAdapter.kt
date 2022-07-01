package com.brainoptimax.peakmeup.adapter.emotions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.Emotion
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemEmotionDailyBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DailyEmotionAdapter(private var emotionsList: List<Emotion>?) : RecyclerView.Adapter<DailyEmotionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_emotion_daily, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emotionsList!![position])
    }

    override fun getItemCount(): Int {
        return if (emotionsList != null) {
            emotionsList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemEmotionDailyBinding.bind(view)

        @SuppressLint("SimpleDateFormat")
        fun bind(emotion: Emotion) {
            val emotionName = emotion.emotionName
            val emotionDate = emotion.emotionDate
            val emotionNote = emotion.emotionNote

            val dateFormat = SimpleDateFormat("E, d MMMM yyyy - h:mm")
            try {
                val d = dateFormat.parse(emotionDate)
                val sdf = SimpleDateFormat("h:mm", Locale.getDefault())
                val emotionTime = sdf.format(d)

                if (emotionNote!!.isNotEmpty() && emotionNote != "null") {
                    binding.tvItemDescEmotion.text = emotionNote
                    binding.tvTimeEmotion.text = emotionTime
                } else {
                    binding.tvTimeEmotion.visibility = View.INVISIBLE
                    binding.tvItemDescEmotion.text = emotionNote
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }


            when (emotionName) {
                "Excited" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_exited)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.excited)
                }
                "Enthusiastic" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_enthusiastic)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.enthusiastic)
                }
                "Happy" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_happy)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.happy)
                }
                "Grateful" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_grateful)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.grateful)
                }
                "Passionate" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_passionate)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.passionate)
                }
                "Joyful" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_joyful)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.joyful)
                }
                "Brave" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_brave)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.brave)
                }
                "Confident" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_confident)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.confident)
                }
                "Proud" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_proud)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.proud)
                }
                "Hopeful" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_hopeful)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.hopeful)
                }
                "Optimistic" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_optimistic)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.optimistic)
                }
                "Calm" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_calm)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.calm)
                }
                "Fun" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_fun)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.`fun`)
                }
                "Awful" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_awful)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.awful)
                }
                "Good" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_good)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.good)
                }
                "Numb" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_numb)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.numb)
                }
                "Bad" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_bad)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.bad)
                }
                "Bored" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_bored)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.bored)
                }
                "Tired" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_tired)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.tired)
                }
                "Frustrated" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_frustrated)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.frustrated)
                }
                "Stressed" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_stressed)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.stressed)
                }
                "Insecure" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_insecure)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.insecure)
                }
                "Angry" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_angry)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.angry)
                }
                "Sad" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_sad)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.sad)
                }
                "Afraid" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_afraid)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.afraid)
                }
                "Envious" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_envious)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.envious)
                }
                "Anxious" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_anxious)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.anxious)
                }
                "Down" -> {
                    binding.ivItemEmotion.setImageResource(R.drawable.ic_down)
                    binding.tvItemEmotion.text = itemView.context.getString(R.string.down)
                }
            }
        }
    }
}