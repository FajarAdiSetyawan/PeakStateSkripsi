package com.brainoptimax.peakmeup.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakmeup.ui.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityResultPeakQuizBinding

class ResultPeakQuizActivity : AppCompatActivity() {

    private var activityResultPeakQuizBinding: ActivityResultPeakQuizBinding? = null
    private val binding get() = activityResultPeakQuizBinding!!

    private lateinit var viewModel: QuizViewModel
    private  var resultQuiz: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPeakQuizBinding = ActivityResultPeakQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_tosca)

        viewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]

        binding.btnDone.setOnClickListener {
            moveMain()
        }

        binding.btnAgain.setOnClickListener {
            startActivity(Intent(this, IntroPeakStateQuizActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        showLoading()
        viewModel.peakQuizResult
        viewModel.peakQuizMutableLiveData.observe(this){ peak ->
            hideLoading()
            if (peak == "null" || peak!!.isEmpty() || peak.equals(null)){
                moveMain()
            }else{
                resultQuiz = peak
                if (peak == "Low") {
                    binding.tvResult.text = resources.getString(R.string.low_readiness)
                    binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_1)
                }
                if (resultQuiz == "Medium") {
                    binding.tvResult.text = resources.getString(R.string.medium_readiness)
                    binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_2)
                }
                if (resultQuiz == "High") {
                    binding.tvResult.text = resources.getString(R.string.high_readiness)
                    binding.tvDescResultQuiz.text = resources.getString(R.string.result_peak_3)
                }
            }
        }
        viewModel.databaseErrorPeak.observe(this){ error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }



        viewModel.databaseErrorPeak.observe(this
        ) { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.btnRecomendation.setOnClickListener {
            when {
                resultQuiz.equals("Low") -> {
                    moveRecommend()
                }
                resultQuiz.equals("Medium") -> {
                    moveRecommend()
                }
                resultQuiz.equals("High") -> {
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

    private fun showLoading(){
        binding.shimmerResult.visibility = View.VISIBLE
        binding.shimmerResult.startShimmer()
        binding.shimmerDescResult.visibility = View.VISIBLE
        binding.shimmerDescResult.startShimmer()
        binding.tvResult.visibility = View.GONE
        binding.tvDescResultQuiz.visibility = View.GONE
    }

    private fun hideLoading(){
        binding.shimmerResult.visibility = View.GONE
        binding.shimmerResult.stopShimmer()
        binding.shimmerDescResult.visibility = View.GONE
        binding.shimmerDescResult.stopShimmer()
        binding.tvResult.visibility = View.VISIBLE
        binding.tvDescResultQuiz.visibility = View.VISIBLE
    }
}