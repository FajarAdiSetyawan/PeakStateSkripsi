package com.brainoptimax.peakstate.adapter.emotions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemEmotionDailyBinding
import com.brainoptimax.peakstate.model.Emotion
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DailyEmotionAdapter(var emotionsList: List<Emotion>?) : RecyclerView.Adapter<DailyEmotionAdapter.ViewHolder>() {

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
            with(binding){

                val emotionName = emotion.emotionName
                val emotionDate = emotion.emotionDate
                val emotionNote = emotion.emotionNote

                val dateFormat = SimpleDateFormat("E, d MMMM yyyy - h:mm")
                try {
                    val d = dateFormat.parse(emotionDate)
                    val sdf = SimpleDateFormat("h:mm", Locale.getDefault())
                    val emotionTime = sdf.format(d)

                    binding.tvItemEmotion.text = emotionName
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
                    }
                    "Enthusiastic" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_enthusiastic)
                    }
                    "Happy" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_happy)
                    }
                    "Grateful" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_grateful)
                    }
                    "Passionate" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_passionate)
                    }
                    "Joyful" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_joyful)
                    }
                    "Brave" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_brave)
                    }
                    "Confident" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_confident)
                    }
                    "Proud" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_proud)
                    }
                    "Hopeful" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_hopeful)
                    }
                    "Optimistic" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_optimistic)
                    }
                    "Calm" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_calm)
                    }
                    "Fun" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_fun)
                    }
                    "Awful" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_awful)
                    }
                    "Good" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_good)
                    }
                    "Numb" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_numb)
                    }
                    "Bad" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_bad)
                    }
                    "Bored" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_bored)
                    }
                    "Tired" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_tired)
                    }
                    "Frustrated" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_frustrated)
                    }
                    "Stressed" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_stressed)
                    }
                    "Insecure" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_insecure)
                    }
                    "Angry" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_angry)
                    }
                    "Sad" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_sad)
                    }
                    "Afraid" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_afraid)
                    }
                    "Envious" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_envious)
                    }
                    "Anxious" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_anxious)
                    }
                    "Down" -> {
                        binding.ivItemEmotion.setImageResource(R.drawable.ic_down)
                    }
                }
            }
        }
    }
}