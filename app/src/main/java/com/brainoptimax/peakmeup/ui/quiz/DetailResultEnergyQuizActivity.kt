package com.brainoptimax.peakmeup.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakmeup.ui.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.ConnectionType
import com.brainoptimax.peakmeup.utils.NetworkMonitorUtil
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityResultEnergyQuizBinding
import com.brainoptimax.peakmeup.ui.dashboard.activator.DetailActivatorActivity

class DetailResultEnergyQuizActivity : AppCompatActivity() {

    private var activityResultPsrQuizBinding: ActivityResultEnergyQuizBinding? = null
    private val binding get() = activityResultPsrQuizBinding!!

    private var resultQuiz: String? = null

    companion object {
        const val EXTRA_ENERGY = "extra_energy"
        const val EXTRA_TENSION = "extra_tension"
        const val EXTRA_DATE = "extra_date"
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPsrQuizBinding = ActivityResultEnergyQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_orange_400)

        val energy = intent.getStringExtra(EXTRA_ENERGY)
        val tension = intent.getStringExtra(EXTRA_TENSION)
        val datetime = intent.getStringExtra(EXTRA_DATE)

        binding.tvDateTime.text = datetime

        binding.btnDone.setOnClickListener {
            moveBack()
        }

        binding.tvEnergy.text = energy + " " + resources.getString(R.string.energy)
        binding.tvTension.text = tension + " " + resources.getString(R.string.tension)

        binding.btnAgain.setOnClickListener {
            startActivity(Intent(this, IntroEnergyTensionActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }


        if (energy == "Low" && tension == "Low") {
            binding.tvTraining.text = resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text =  resources.getString(R.string.result_energy_1)
        }
        if (energy == "Moderate" && tension == "Low") {
            binding.tvTraining.text =  resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_2)
        }
        if (energy == "Low" && tension == "Moderate") {
            binding.tvTraining.text =  resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_3)
        }
        if (energy == "Moderate" && tension == "Moderate") {
            binding.tvTraining.text = resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_4)
        }
        if (energy == "Low" && tension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_5)
        }
        if (energy == "Moderate" && tension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_6)
        }
        if (energy == "High" && tension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_7)
        }
        if (energy == "High" && tension == "Low") {
            binding.tvTraining.text = resources.getString(R.string.training_maintenance)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_8)
        }
        if (energy == "High" && tension == "Moderate") {
            binding.tvTraining.text = resources.getString(R.string.training_maintenance)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_9)
        }

        binding.btnRecomendation.setOnClickListener {
            resultQuiz = binding.tvTraining.text.toString()

            when {
                resultQuiz.equals(resources.getString(R.string.training_energizer)) -> {
                    moveRecommend()
                }
                resultQuiz.equals(resources.getString(R.string.training_relaxation)) -> {
                    moveRecommend()
                }
                resultQuiz.equals(resources.getString(R.string.training_maintenance)) -> {
                    moveRecommend()
                }
            }
        }
    }

    private fun moveRecommend(){
        val choose = Intent(this, RecommendEnergyQuizActivity::class.java)
        choose.putExtra("result", resultQuiz)
        startActivity(choose)
        Animatoo.animateSlideUp(this)
    }

    private fun moveBack(){
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