package com.brainoptimax.peakstate.ui.quiz

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
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityResultEnergyQuizBinding
import com.brainoptimax.peakstate.ui.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.brainoptimax.peakstate.viewmodel.quiz.QuizViewModel

class ResultEnergyQuizActivity : AppCompatActivity() {

    private var activityResultPsrQuizBinding: ActivityResultEnergyQuizBinding? = null
    private val binding get() = activityResultPsrQuizBinding!!

    private lateinit var viewModel: QuizViewModel

    private var resultEnergy: String = ""
    private var resultTension: String = ""
    private var resultQuiz: String? = null

    private val networkMonitor = NetworkMonitorUtil(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPsrQuizBinding = ActivityResultEnergyQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_orange_400)
        checkConnection()

        viewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]

        binding.btnDone.setOnClickListener {
            moveBack()
        }

        binding.btnAgain.setOnClickListener {
            startActivity(Intent(this, IntroEnergyTensionActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        showLoading()
        viewModel.energyQuizResult
        viewModel.energyQuizMutableLiveData.observe(this){ energy ->
            hideLoading()
            if (energy == "null" || energy!!.isEmpty() || energy.equals(null)){
                moveBack()
            }else{
                binding.tvEnergy.text = "$energy Energy"
                resultEnergy = energy
            }
        }

        viewModel.databaseErrorEnergy.observe(this
        ) { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.tensionQuizResult
        viewModel.tensionQuizMutableLiveData.observe(this){ tension ->
            if (tension == "null" || tension!!.isEmpty() || tension.equals(null)){
                moveBack()
            }else{
                binding.tvTension.text = "$tension Tension"
                resultTension = tension
            }
        }
        viewModel.databaseErrorTension.observe(this
        ) { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }

        if (resultTension == "Low" && resultTension == "Low") {
            binding.tvTraining.text = resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text =  resources.getString(R.string.result_energy_1)
        }
        if (resultTension == "Moderate" && resultTension == "Low") {
            binding.tvTraining.text =  resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_2)
        }
        if (resultTension == "Low" && resultTension == "Moderate") {
            binding.tvTraining.text =  resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_3)
        }
        if (resultTension == "Moderate" && resultTension == "Moderate") {
            binding.tvTraining.text = resources.getString(R.string.training_energizer)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_4)
        }
        if (resultTension == "Low" && resultTension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_5)
        }
        if (resultTension == "Moderate" && resultTension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_6)
        }
        if (resultTension == "High" && resultTension == "High") {
            binding.tvTraining.text = resources.getString(R.string.training_relaxation)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_7)
        }
        if (resultTension == "High" && resultTension == "Low") {
            binding.tvTraining.text = resources.getString(R.string.training_maintenance)
            binding.tvDescResultQuiz.text = resources.getString(R.string.result_energy_8)
        }
        if (resultTension == "High" && resultTension == "Moderate") {
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

    // fungsi untuk mengecek internet secara berulang
    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    // fungsi untuk menghentikan pengecekan internet
    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    // Check Internet Connection
    private fun checkConnection() {
        // buat dialog error internet
        val li = LayoutInflater.from(this)
        // panggil layout dialog error
        val promptsView: View = li.inflate(R.layout.dialog_error, null)
        val alertDialogBuilder = android.app.AlertDialog.Builder(
            this
        )
        alertDialogBuilder.setView(promptsView)
        // dialog error tidak dapat di tutup selain menekan tombol close
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20)

        // panggil textview judul error dari layout dialog
        val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_error) as TextView
        titleView.text = (resources.getString(R.string.offline))

        // panggil textview desc error dari layout dialog
        val descView = promptsView.findViewById<View>(R.id.tv_desc_dialog_error) as TextView
        descView.text = (resources.getString(R.string.msgoffline))

        // panggil animasi lottie error dari layout dialog
        val lottieView =
            promptsView.findViewById<View>(R.id.lottie_dialog_error) as LottieAnimationView
        lottieView.playAnimation()
        lottieView.setAnimation(R.raw.offline)

        // panggil button error dari layout dialog
        val btnView = promptsView.findViewById<View>(R.id.btn_error_dialog) as Button
        btnView.text = (resources.getString(R.string.connect))
        btnView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            this.let { it1 -> Animatoo.animateSlideUp(it1) }
        }
        // panggil gambar close error dari layout dialog
        val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_error_dialog) as ImageView
        ivDismiss.setOnClickListener {
            networkMonitor.result = { isAvailable, type ->
                runOnUiThread {
                    // cek ada status wifi/data seluler
                    when (isAvailable) {
                        true -> {
                            when (type) {
                                ConnectionType.Wifi -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                    // dialog error ditutup
                                    alertDialog.dismiss()
                                }
                                ConnectionType.Cellular -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                    alertDialog.dismiss()
                                }
                                else -> {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                        false -> {
                            Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                            // dialog error ditampilkan
                            alertDialog.show()
                        }
                    }
                }
            }
        }
        alertDialog.window!!.setBackgroundDrawable(inset)
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                alertDialog.dismiss()
                            }
                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                alertDialog.dismiss()
                            }
                            else -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        alertDialog.show()
                    }
                }
            }
        }
    }

    private fun showLoading(){
        binding.shimmerEnergy.visibility = View.VISIBLE
        binding.shimmerEnergy.startShimmer()
        binding.shimmerTension.visibility = View.VISIBLE
        binding.shimmerTension.startShimmer()
        binding.shimmerDescResult.visibility = View.VISIBLE
        binding.shimmerDescResult.startShimmer()
        binding.tvEnergy.visibility = View.GONE
        binding.tvDescResultQuiz.visibility = View.GONE
        binding.tvTension.visibility = View.GONE
    }

    private fun hideLoading(){
        binding.shimmerEnergy.visibility = View.GONE
        binding.shimmerEnergy.stopShimmer()
        binding.shimmerTension.visibility = View.GONE
        binding.shimmerTension.stopShimmer()
        binding.shimmerDescResult.visibility = View.GONE
        binding.shimmerDescResult.stopShimmer()
        binding.tvEnergy.visibility = View.VISIBLE
        binding.tvDescResultQuiz.visibility = View.VISIBLE
        binding.tvTension.visibility = View.VISIBLE
    }
}