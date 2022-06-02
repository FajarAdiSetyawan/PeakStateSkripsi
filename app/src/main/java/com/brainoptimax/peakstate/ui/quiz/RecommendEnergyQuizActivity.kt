package com.brainoptimax.peakstate.ui.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityRecommendEnergyQuizBinding
import com.brainoptimax.peakstate.utils.Animatoo

class RecommendEnergyQuizActivity : AppCompatActivity() {
    private var activityRecommendEnergyQuizBinding: ActivityRecommendEnergyQuizBinding? = null
    private val binding get() = activityRecommendEnergyQuizBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRecommendEnergyQuizBinding = ActivityRecommendEnergyQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_orange_400)
        val intent = intent
        val result = intent.getStringExtra("result")

        if (result.equals(null) || result!!.isEmpty() || result == "null"){
            moveMain()
        }

        when {
            result.equals(resources.getString(R.string.training_energizer)) -> {
                binding.tvTextCard1.text = resources.getString(R.string.energizer)
                binding.expandText1.text = resources.getString(R.string.recommend_energizer_1)
                binding.expandText2.text = resources.getString(R.string.recommend_energizer_2)
                binding.expandText3.text = resources.getString(R.string.recommend_energizer_3)
                binding.expandText4.text = resources.getString(R.string.recommend_energizer_4)
                binding.expandText5.text = resources.getString(R.string.recommend_energizer_5)
                binding.expandText6.text = resources.getString(R.string.recommend_energizer_6)
            }
            result.equals(resources.getString(R.string.training_relaxation)) -> {
                binding.cardBreath.visibility = View.VISIBLE
                binding.cardPractise.visibility = View.VISIBLE
                binding.tvTextCard1.text = resources.getString(R.string.relaxation)
                binding.icLow.setImageResource(R.drawable.ic_meditate)
                binding.expandText1.text = resources.getString(R.string.recommend_relax_1)
                binding.expandTextBreath.text = resources.getString(R.string.recommend_relax_2)
                binding.expandText2.text = resources.getString(R.string.recommend_relax_3)
                binding.tvTextCard2.text = resources.getString(R.string.breathe)
                binding.expandText3.text = resources.getString(R.string.recommend_relax_4)
                binding.expandText4.text = resources.getString(R.string.recommend_relax_5)
                binding.expandText5.text = resources.getString(R.string.recommend_relax_6)
                binding.expandText6.text = resources.getString(R.string.recommend_relax_7)
                binding.expandTextPractise.text = resources.getString(R.string.recommend_relax_8)
            }
            result.equals(resources.getString(R.string.training_maintenance)) -> {
                binding.tvTextCard1.text = resources.getString(R.string.maintenance)
                binding.icLow.setImageResource(R.drawable.ic_star)
                binding.expandText1.text = resources.getString(R.string.recommend_maintenance_1)
                binding.expandText2.text = resources.getString(R.string.recommend_maintenance_2)
                binding.expandText3.text = resources.getString(R.string.recommend_maintenance_3)
                binding.expandText4.text = resources.getString(R.string.recommend_maintenance_4)
                binding.expandText5.text = resources.getString(R.string.recommend_maintenance_5)
                binding.expandText6.text = resources.getString(R.string.recommend_maintenance_6)
            }
        }

        binding.btnBackMenu.setOnClickListener {
            moveMain()
        }
    }

    private fun moveMain(){
        startActivity(Intent(this, ResultEnergyQuizActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ResultEnergyQuizActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }
}