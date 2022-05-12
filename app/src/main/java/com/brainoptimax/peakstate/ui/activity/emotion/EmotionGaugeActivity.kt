package com.brainoptimax.peakstate.ui.activity.emotion

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityEmotionGaugeBinding
import com.brainoptimax.peakstate.ui.activity.auth.RegisterActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroEmotionActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmotionGaugeActivity : AppCompatActivity() {

    private var binding: ActivityEmotionGaugeBinding? = null
    private val activityEmotionGaugeBinding get() = binding!!

    private val networkMonitor = NetworkMonitorUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadTheme()
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)

        binding = ActivityEmotionGaugeBinding.inflate(layoutInflater)
        val view = activityEmotionGaugeBinding.root
        setContentView(view)

        activityEmotionGaugeBinding.bottomNavigationView.background = null
        activityEmotionGaugeBinding.bottomNavigationView.menu.getItem(1).isEnabled = false

        val dailyEmotion = DailyEmotionFragment()
        val weeklyEmotion = WeeklyEmotionFragment()

        setCurrentFragment(dailyEmotion)

        activityEmotionGaugeBinding.bottomNavigationView.isItemActiveIndicatorEnabled = true

        activityEmotionGaugeBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            activityEmotionGaugeBinding.bottomNavigationView.isItemActiveIndicatorEnabled = true
            when (it.itemId) {
                R.id.menuDaily -> setCurrentFragment(dailyEmotion)
                R.id.menuWeekly -> setCurrentFragment(weeklyEmotion)

            }
            true
        }
        checkConnection()

        activityEmotionGaugeBinding.fab.setOnClickListener {
            startActivity(Intent(this, ChooseEmotionsActivity::class.java))
            Animatoo.animateSlideUp(this)
            finish()
        }

        activityEmotionGaugeBinding.btnBackMenu.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        activityEmotionGaugeBinding.btnInfoEmotion.setOnClickListener {
            startActivity(Intent(this, IntroEmotionActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =

        supportFragmentManager.beginTransaction().apply {
            activityEmotionGaugeBinding.bottomNavigationView.isItemActiveIndicatorEnabled = true
            replace(R.id.fragment_container_emotion, fragment)
            commit()
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

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
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