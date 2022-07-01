package com.brainoptimax.peakmeup.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityResultPeakQuizBinding
import com.brainoptimax.peakmeup.ui.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel

class DetailResultPeakQuizActivity : AppCompatActivity() {

    private var activityResultPeakQuizBinding: ActivityResultPeakQuizBinding? = null
    private val binding get() = activityResultPeakQuizBinding!!

    private  var resultQuiz: String? = null

    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_DATE = "extra_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPeakQuizBinding = ActivityResultPeakQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_tosca)

        val peak = intent.getStringExtra(EXTRA_RESULT)
        val datetime = intent.getStringExtra(EXTRA_DATE)

        binding.tvDateTime.text = datetime

        binding.btnDone.setOnClickListener {
            moveMain()
        }

        if(peak!!.isEmpty() || peak.equals(null) || peak.equals("null")){
            moveMain()
        }

        binding.btnAgain.setOnClickListener {
            startActivity(Intent(this, IntroPeakStateQuizActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        if (peak == "Low") {
            resultQuiz = peak
            binding.tvResult.text = resources.getString(R.string.low_readiness)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_1)
        }
        if (peak == "Medium") {
            resultQuiz = peak
            binding.tvResult.text = resources.getString(R.string.medium_readiness)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_2)
        }
        if (peak == "High") {
            resultQuiz = peak
            binding.tvResult.text = resources.getString(R.string.high_readiness)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_3)
        }

        binding.btnRecomendation.setOnClickListener {
            when {
                peak.equals("Low") -> {
                    moveRecommend()
                }
                peak.equals("Medium") -> {
                    moveRecommend()
                }
                peak.equals("High") -> {
                    moveRecommend()
                }
            }
        }
    }


    private fun moveRecommend(){
        val choose = Intent(this, RecommendPeakActivity::class.java)
        choose.putExtra("result", resultQuiz)
        startActivity(choose)
        Animatoo.animateSlideUp(this)
    }

    private fun moveMain(){
        startActivity(Intent(this, QuizActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, QuizActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }

}