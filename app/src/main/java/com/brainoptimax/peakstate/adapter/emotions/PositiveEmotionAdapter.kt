package com.brainoptimax.peakstate.adapter.emotions

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ItemEmotionsBinding
import com.brainoptimax.peakstate.model.ListEmotions
import com.brainoptimax.peakstate.ui.activity.emotion.SaveEmotionsActivity
import com.brainoptimax.peakstate.utils.Animatoo

class PositiveEmotionAdapter(var context: Context) : RecyclerView.Adapter<PositiveEmotionAdapter.ViewHolder>() {

    var emotionsList = emptyList<ListEmotions>()

    internal fun setDataList(dataList: List<ListEmotions>) {
        this.emotionsList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_emotions, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(emotionsList[position])
    }

    override fun getItemCount(): Int = emotionsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemEmotionsBinding.bind(view)

        fun bind(listEmotions: ListEmotions){
            binding.tvEmotion.text = listEmotions.emotions
            binding.ivEmotion.setImageResource(listEmotions.image)

            itemView.setOnClickListener {
                val intent = Intent(context, SaveEmotionsActivity::class.java)
                intent.putExtra(SaveEmotionsActivity.EXTRA_EMOTION, listEmotions.emotions)
                intent.putExtra(SaveEmotionsActivity.EXTRA_CONDITION, "Positive")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                itemView.context.startActivity(intent)
                Animatoo.animateSlideLeft(itemView.context)
            }
        }


    }
}