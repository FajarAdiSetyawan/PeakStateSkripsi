package com.brainoptimax.peakstate.ui.quiz

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityQuizBinding
import com.brainoptimax.peakstate.ui.MainActivity
import com.brainoptimax.peakstate.ui.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakstate.ui.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.brainoptimax.peakstate.viewmodel.quiz.QuizViewModel

class QuizActivity : AppCompatActivity() {

    private var activityQuizBinding: ActivityQuizBinding? = null
    private val binding get() = activityQuizBinding!!

    private val networkMonitor = NetworkMonitorUtil(this)

    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadTheme()

        activityQuizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkConnection()

        viewModel = ViewModelProviders.of(this)[QuizViewModel::class.java]

        binding.btnLater.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        binding.btnEt.setOnClickListener {
            viewModel.energyQuizResult
            viewModel.energyQuizMutableLiveData.observe(this){ energy ->
                if (energy == "null" || energy!!.isEmpty() || energy.equals(null)){
                    startActivity(Intent(this, IntroEnergyTensionActivity::class.java))
                    Animatoo.animateSlideLeft(this)
                    finish()
                }else{
                    startActivity(Intent(this, ResultEnergyQuizActivity::class.java))
                    Animatoo.animateSlideLeft(this)
                    finish()
                }
            }
        }

        binding.btnPsr.setOnClickListener {
            viewModel.peakQuizResult
            viewModel.peakQuizMutableLiveData.observe(this){ peak ->
                if (peak == "null" || peak!!.isEmpty() || peak.equals(null)){
                    startActivity(Intent(this, IntroPeakStateQuizActivity::class.java))
                    Animatoo.animateSlideLeft(this)
                    finish()
                }else{
                    startActivity(Intent(this, ResultPeakQuizActivity::class.java))
                    Animatoo.animateSlideLeft(this)
                    finish()
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        Animatoo.animateSwipeRight(this)
        finish()
    }

    private fun loadTheme() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val theme = preference.getString(
            getString(R.string.pref_key_theme),
            getString(R.string.pref_theme_entry_auto)
        )
        when {
            theme.equals(getString(R.string.pref_theme_entry_auto)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            theme.equals(getString(R.string.pref_theme_entry_dark)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            theme.equals(getString(R.string.pref_theme_entry_light)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateTheme(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        return true
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