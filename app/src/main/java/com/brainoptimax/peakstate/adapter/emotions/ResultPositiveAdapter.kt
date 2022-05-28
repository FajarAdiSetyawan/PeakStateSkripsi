package com.brainoptimax.peakstate.adapter.emotions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemResultEmotionsBinding
import com.brainoptimax.peakstate.model.Emotion

class ResultPositiveAdapter(var emotionList: List<Emotion>?) :
    RecyclerView.Adapter<ResultPositiveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_result_emotions, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emotionList!![position])
    }

    override fun getItemCount(): Int {
        return if (emotionList != null) {
            emotionList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemResultEmotionsBinding.bind(view)

        fun bind(emotion: Emotion) {
            val totalAllEmotion = emotion.totalAllEmotion
            val emotionName = emotion.emotionName
            val totalPerEmotion = emotion.totalPerEmotion

            binding.tvCountEmotions.text = totalPerEmotion.toString()
            binding.tvEmotion.text = emotionName

            binding.pbEmotions.progress = 0
            binding.pbEmotions.max = totalAllEmotion!!
            binding.pbEmotions.progress = totalPerEmotion!!



            when (emotionName) {
                "Excited" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_exited)
                }
                "Enthusiastic" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_enthusiastic)
                }
                "Happy" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_happy)
                }
                "Grateful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_grateful)
                }
                "Passionate" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_passionate)
                }
                "Joyful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_joyful)
                }
                "Brave" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_brave)
                }
                "Confident" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_confident)
                }
                "Proud" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_proud)
                }
                "Hopeful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_hopeful)
                }
                "Optimistic" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_optimistic)
                }
                "Calm" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_calm)
                }
                "Fun" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_fun)
                }
                "Awful" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_awful)
                }
                "Good" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_good)
                }
            }
        }
    }
}