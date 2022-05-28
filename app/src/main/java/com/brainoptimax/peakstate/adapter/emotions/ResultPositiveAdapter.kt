package com.brainoptimax.peakstate.adapter.emotions

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemResultEmotionsBinding
import com.brainoptimax.peakstate.model.Emotion
import com.brainoptimax.peakstate.viewmodel.emotion.EmotionViewModel

class ResultPositiveAdapter(var emotionList: List<Emotion>?, activity: FragmentActivity) :
    RecyclerView.Adapter<ResultPositiveAdapter.ViewHolder>() {

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

            binding.tvCountEmotions.text = totalPerEmotion.toString()
            binding.tvEmotion.text = emotionName

            viewModel.totalAllEmotion
            viewModel.totalAllEmotionMutableLiveData.observe(activity) { totalAllEmotion ->
                Log.d("TAG", "totalAllEmotion: $totalAllEmotion")
                if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                    totalAll = 0
                } else {
                    totalAll = totalAllEmotion.toInt()
                }
            }


            binding.pbEmotions.progress = 0
            binding.pbEmotions.max = totalAll
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