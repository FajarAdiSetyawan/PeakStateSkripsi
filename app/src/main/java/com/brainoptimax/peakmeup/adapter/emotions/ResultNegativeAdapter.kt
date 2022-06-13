package com.brainoptimax.peakmeup.adapter.emotions

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.model.Emotion
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemResultEmotionsBinding

class ResultNegativeAdapter(private var emotionList: List<Emotion>?, activity: FragmentActivity) :
    RecyclerView.Adapter<ResultNegativeAdapter.ViewHolder>() {

    private lateinit var viewModel: EmotionViewModel
    private var totalAll = 0
    private val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewModel = ViewModelProviders.of(activity)[EmotionViewModel::class.java]

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
            val emotionName = emotion.emotionName
            val totalPerEmotion = emotion.totalPerEmotion

            viewModel.totalAllEmotion
            viewModel.totalAllEmotionMutableLiveData.observe(activity) { totalAllEmotion ->
                Log.d("TAG", "totalAllEmotion: $totalAllEmotion")
                totalAll = if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                    0
                } else {
                    totalAllEmotion.toInt()
                }
            }

            binding.pbEmotions.progress = 0
            binding.pbEmotions.max = totalAll
            binding.pbEmotions.progress = totalPerEmotion!!

            binding.tvCountEmotions.text = totalPerEmotion.toString()
            binding.tvEmotion.text = emotionName

            when (emotionName) {
                "Numb" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_numb)
                }
                "Bad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bad)
                }
                "Bored" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bored)
                }
                "Tired" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_tired)
                }
                "Frustrated" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_frustrated)
                }
                "Stressed" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_stressed)
                }
                "Insecure" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_insecure)
                }
                "Angry" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_angry)
                }
                "Sad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_sad)
                }
                "Afraid" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_afraid)
                }
                "Envious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_envious)
                }
                "Anxious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_anxious)
                }
                "Down" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_down)
                }
            }

        }
    }
}