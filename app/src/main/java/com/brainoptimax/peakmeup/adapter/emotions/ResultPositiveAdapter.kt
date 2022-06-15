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
import com.brainoptimax.peakmeup.utils.Preferences

class ResultPositiveAdapter(
    private var emotionList: List<Emotion>?,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<ResultPositiveAdapter.ViewHolder>() {

    private lateinit var viewModel: EmotionViewModel
    private var totalAll = 0
    private lateinit var preference: Preferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewModel = ViewModelProviders.of(activity)[EmotionViewModel::class.java]
        preference = Preferences(activity)

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
        val uidUser = preference.getValues("uid")

        fun bind(emotion: Emotion) {
            val emotionName = emotion.emotionName
            val totalPerEmotion = emotion.totalPerEmotion

            binding.tvCountEmotions.text = totalPerEmotion.toString()
            binding.tvEmotion.text = emotionName

            viewModel.totalAllEmotion(uidUser!!)
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