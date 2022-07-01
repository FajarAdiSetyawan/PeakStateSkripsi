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

class ResultNegativeAdapter(private var emotionList: List<Emotion>?, activity: FragmentActivity) :
    RecyclerView.Adapter<ResultNegativeAdapter.ViewHolder>() {

    private lateinit var viewModel: EmotionViewModel
    private var totalAll = 0
    private val activity = activity

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

            binding.tvCountEmotions.text = totalPerEmotion.toString()

            when (emotionName) {
                "Numb" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_numb)
                    binding.tvEmotion.text = itemView.context.getString(R.string.numb)
                }
                "Bad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bad)
                    binding.tvEmotion.text = itemView.context.getString(R.string.bad)
                }
                "Bored" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_bored)
                    binding.tvEmotion.text = itemView.context.getString(R.string.bored)
                }
                "Tired" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_tired)
                    binding.tvEmotion.text = itemView.context.getString(R.string.tired)
                }
                "Frustrated" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_frustrated)
                    binding.tvEmotion.text = itemView.context.getString(R.string.frustrated)
                }
                "Stressed" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_stressed)
                    binding.tvEmotion.text = itemView.context.getString(R.string.stressed)
                }
                "Insecure" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_insecure)
                    binding.tvEmotion.text = itemView.context.getString(R.string.insecure)
                }
                "Angry" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_angry)
                    binding.tvEmotion.text = itemView.context.getString(R.string.angry)
                }
                "Sad" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_sad)
                    binding.tvEmotion.text = itemView.context.getString(R.string.sad)
                }
                "Afraid" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_afraid)
                    binding.tvEmotion.text = itemView.context.getString(R.string.afraid)
                }
                "Envious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_envious)
                    binding.tvEmotion.text = itemView.context.getString(R.string.envious)
                }
                "Anxious" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_anxious)
                    binding.tvEmotion.text = itemView.context.getString(R.string.anxious)
                }
                "Down" -> {
                    binding.ivEmotion.setImageResource(R.drawable.ic_down)
                    binding.tvEmotion.text = itemView.context.getString(R.string.down)
                }
            }

        }
    }
}