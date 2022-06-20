package com.brainoptimax.peakmeup.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.ConnectionType
import com.brainoptimax.peakmeup.utils.NetworkMonitorUtil
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityPeakQuizBinding
import com.brainoptimax.peakmeup.utils.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import render.animations.Fade
import render.animations.Render
import render.animations.Slide
import render.animations.Zoom

class PeakQuizActivity : AppCompatActivity() {
    private var activityPeakQuizBinding : ActivityPeakQuizBinding? = null
    private val binding get() = activityPeakQuizBinding!!


    private val networkMonitor = NetworkMonitorUtil(this)

    private var no = 1
    private var rResult: String? = null
    private var mResult = 0

    private val animation = Render(this)

    private lateinit var viewModel: QuizViewModel

    private lateinit var preference: Preferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityPeakQuizBinding = ActivityPeakQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preference = Preferences(this)
        val uidUser = preference.getValues("uid")

        checkConnection()

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_tosca)

        viewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]

        selectCardAnswer()
        selectAnswer()

        binding.next.setOnClickListener {
            if (binding.cbAnswer1.isChecked || binding.cbAnswer2.isChecked || binding.cbAnswer3.isChecked || binding.cbAnswer4.isChecked || binding.cbAnswer5.isChecked) {
                no++
                if (no == 2) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet2.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_2)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 3) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet3.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_3)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 4) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet4.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_4)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 5) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet5.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_5)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 6) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet6.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_6)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 7) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet7.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_7)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 8) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet8.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_8)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 9) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet9.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_9)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 10) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet10.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_10)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 11) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet11.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_11)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 12) {
                    animQuestion()
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4
                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    binding.tvResult.text = "Result = $mResult"
                    selectAnswer()
                    binding.bulet12.setImageResource(R.drawable.circle_btn_dark_tosca)
                    binding.tvQuestion.text = resources.getString(R.string.psr_question_12)
                    binding.nomer.text = "Nomor = $no"
                }
                if (no == 13) {
                    when {
                        binding.cbAnswer1.isChecked -> mResult += 5
                        binding.cbAnswer2.isChecked -> mResult += 4

                        binding.cbAnswer3.isChecked -> mResult += 3
                        binding.cbAnswer4.isChecked -> mResult += 3
                        binding.cbAnswer5.isChecked -> mResult += 1
                    }
                    when {
                        mResult >= 48 -> rResult = "High"
                        mResult >= 31 -> rResult = "Medium"
                        mResult >= 12 -> rResult = "Low"
                    }
                    viewModel.openLoadingDialog(this)
                    viewModel.savePeakQuiz(uidUser!!, rResult)
                    viewModel.addPeakQuizMutableLiveData.observe(this){ status ->
                        if (status.equals("success")){
                            viewModel.closeLoadingDialog()

                            startActivity(Intent(this, ResultQuizListActivity::class.java))
                            Animatoo.animateSwipeRight(this)
                            finish()
                        }
                    }

                    viewModel.databaseErrorAddPeak.observe(this) { error ->
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, resources.getString(R.string.select_choise), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun selectCardAnswer(){
        binding.cbAnswer1.setOnClickListener {
            binding.cbAnswer1.isChecked = true
            binding.cbAnswer2.isChecked = false
            binding.cbAnswer3.isChecked = false
            binding.cbAnswer4.isChecked = false
            binding.cbAnswer5.isChecked = false
            binding.cardAnswer1.strokeColor = ContextCompat.getColor(this, R.color.dark_tosca)
            binding.cardAnswer1.strokeWidth = 2
            binding.cardAnswer2.strokeWidth = 0
            binding.cardAnswer3.strokeWidth = 0
            binding.cardAnswer4.strokeWidth = 0
            binding.cardAnswer5.strokeWidth = 0
        }

        binding.cbAnswer2.setOnClickListener {
            binding.cbAnswer1.isChecked = false
            binding.cbAnswer2.isChecked = true
            binding.cbAnswer3.isChecked = false
            binding.cbAnswer4.isChecked = false
            binding.cbAnswer5.isChecked = false
            binding.cardAnswer2.strokeColor = ContextCompat.getColor(this, R.color.dark_tosca)
            binding.cardAnswer1.strokeWidth = 0
            binding.cardAnswer2.strokeWidth = 2
            binding.cardAnswer3.strokeWidth = 0
            binding.cardAnswer4.strokeWidth = 0
            binding.cardAnswer5.strokeWidth = 0
        }

        binding.cbAnswer3.setOnClickListener {
            binding.cbAnswer1.isChecked = false
            binding.cbAnswer2.isChecked = false
            binding.cbAnswer3.isChecked = true
            binding.cbAnswer4.isChecked = false
            binding.cbAnswer5.isChecked = false
            binding.cardAnswer3.strokeColor = ContextCompat.getColor(this, R.color.dark_tosca)
            binding.cardAnswer1.strokeWidth = 0
            binding.cardAnswer2.strokeWidth = 0
            binding.cardAnswer3.strokeWidth = 2
            binding.cardAnswer4.strokeWidth = 0
            binding.cardAnswer5.strokeWidth = 0
        }

        binding.cbAnswer4.setOnClickListener {
            binding.cbAnswer1.isChecked = false
            binding.cbAnswer2.isChecked = false
            binding.cbAnswer3.isChecked = false
            binding.cbAnswer4.isChecked = true
            binding.cbAnswer5.isChecked = false
            binding.cardAnswer4.strokeColor = ContextCompat.getColor(this, R.color.dark_tosca)
            binding.cardAnswer1.strokeWidth = 0
            binding.cardAnswer2.strokeWidth = 0
            binding.cardAnswer3.strokeWidth = 0
            binding.cardAnswer4.strokeWidth = 2
            binding.cardAnswer5.strokeWidth = 0
        }

        binding.cbAnswer5.setOnClickListener {
            binding.cbAnswer1.isChecked = false
            binding.cbAnswer2.isChecked = false
            binding.cbAnswer3.isChecked = false
            binding.cbAnswer4.isChecked = false
            binding.cbAnswer5.isChecked = true
            binding.cardAnswer5.strokeColor = ContextCompat.getColor(this, R.color.dark_tosca)
            binding.cardAnswer1.strokeWidth = 0
            binding.cardAnswer2.strokeWidth = 0
            binding.cardAnswer3.strokeWidth = 0
            binding.cardAnswer4.strokeWidth = 0
            binding.cardAnswer5.strokeWidth = 2
        }
    }

    private fun selectAnswer(){
        binding.cbAnswer1.isChecked = false
        binding.cbAnswer2.isChecked = false
        binding.cbAnswer3.isChecked = false
        binding.cbAnswer4.isChecked = false
        binding.cbAnswer5.isChecked = false
        binding.cardAnswer1.strokeWidth = 0
        binding.cardAnswer2.strokeWidth = 0
        binding.cardAnswer3.strokeWidth = 0
        binding.cardAnswer4.strokeWidth = 0
        binding.cardAnswer5.strokeWidth = 0
    }

    private fun animQuestion(){
        val handler = Handler()
        animation.setAnimation(Slide.OutLeft(binding.cardQuestion))
        animation.start()
        animation.setAnimation(Fade.Out(binding.tvQuestion))
        animation.start()
        binding.tvQuestion.visibility = View.INVISIBLE
        animation.setAnimation(Slide.OutRight(binding.cardAnswer1))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.cardAnswer2))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.cardAnswer3))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.cardAnswer4))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.cardAnswer5))
        animation.start()
        animation.setAnimation(Slide.OutDown(binding.next))
        animation.start()
        handler.postDelayed({
            // cek jika user telah login
            animation.setAnimation(Slide.InRight(binding.cardQuestion))
            animation.start()
            binding.tvQuestion.visibility = View.VISIBLE
            animation.setAnimation(Zoom.In(binding.tvQuestion))
            animation.start()
            animation.setAnimation(Slide.InLeft(binding.cardAnswer1))
            animation.start()
            animation.setAnimation(Slide.InLeft(binding.cardAnswer2))
            animation.start()
            animation.setAnimation(Slide.InLeft(binding.cardAnswer3))
            animation.start()
            animation.setAnimation(Slide.InLeft(binding.cardAnswer4))
            animation.start()
            animation.setAnimation(Slide.InLeft(binding.cardAnswer5))
            animation.start()
            animation.setAnimation(Slide.InUp(binding.next))
            animation.start()
        }, 1000)

    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.exitquiz))
            .setMessage(resources.getString(R.string.msg_exitquiz))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                super.onBackPressed()
                startActivity(Intent(this, QuizActivity::class.java))
                Animatoo.animateSwipeRight(this)
                finish()
            }
            .setIcon(resources.getDrawable(R.drawable.ic_quizzes))
            .show()
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
}