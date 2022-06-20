package com.brainoptimax.peakmeup.adapter.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ItemResultQuizBinding
import com.brainoptimax.peakmeup.model.Quiz
import com.brainoptimax.peakmeup.ui.quiz.DetailResultEnergyQuizActivity

class ResultEnergyQuizAdapter(var context: Context): RecyclerView.Adapter<ResultEnergyQuizAdapter.ViewHolder>() {
    private var quizList: List<Quiz>? = null

    private var onItemClickListener:((Quiz)->Unit)? = null

    fun setEnergyQuiz(quiz: List<Quiz>?) {
        this.quizList = quiz
    }

    fun setOnItemClickListener(listener: (Quiz)->Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_result_quiz, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizList!![position])
    }

    override fun getItemCount(): Int {
        return if (quizList != null) {
            quizList!!.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemResultQuizBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(quiz: Quiz) {
            with(binding){
                tvItem1.text = quiz.energyQuiz + " " + itemView.context.getString(R.string.energy)
                tvItem2.text = quiz.tensionQuiz + " " + itemView.context.getString(R.string.tension)
                tvTimeQuiz.text = quiz.datetime
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailResultEnergyQuizActivity::class.java)
                intent.putExtra(DetailResultEnergyQuizActivity.EXTRA_ENERGY, quiz.energyQuiz)
                intent.putExtra(DetailResultEnergyQuizActivity.EXTRA_TENSION, quiz.tensionQuiz)
                intent.putExtra(DetailResultEnergyQuizActivity.EXTRA_DATE, quiz.datetime)
                itemView.context.startActivity(intent)
            }

            binding.ivDelete.setOnClickListener {
                onItemClickListener?.let{
                    it(quiz)
                }
            }
        }
    }
}