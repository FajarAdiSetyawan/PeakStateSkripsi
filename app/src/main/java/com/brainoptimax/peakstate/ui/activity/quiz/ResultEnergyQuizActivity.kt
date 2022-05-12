package com.brainoptimax.peakstate.ui.activity.quiz

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityResultEnergyQuizBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ResultEnergyQuizActivity : AppCompatActivity() {

    private var activityResultPsrQuizBinding: ActivityResultEnergyQuizBinding? = null
    private val binding get() = activityResultPsrQuizBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var resultQuiz: String? = null

    private val networkMonitor = NetworkMonitorUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPsrQuizBinding = ActivityResultEnergyQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_orange_400)
        checkConnection()

        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }


        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        showLoading()
        databaseReference.child("Users").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    hideLoading()
                    if (dataSnapshot.exists()) {
                        val tension = dataSnapshot.child("Tension").value.toString()
                        val energy = dataSnapshot.child("Energy").value.toString()
                        binding.tvEnergy.text = "$energy Energy"
                        binding.tvTension.text = "$tension Tension"
                        if (energy == "Low" && tension == "Low") {
                            binding.tvTraining.text = "Training Energizer"
                            binding.tvDescResultQuiz.text = "Congratulations! Based on your responses, you are well able to keep your tension in check. However, you’d perform more optimally if you are able to raise and maintain your energy level. Read on for recommendations"
                        }
                        if (energy == "Moderate" && tension == "Low") {
                            binding.tvTraining.text = "Training Energizer"
                            binding.tvDescResultQuiz.text = "Congratulations! Based on your responses, you are well able to keep your tension in check. However, you’d perform more optimally if you are able to raise and maintain your energy level. Read on for recommendations"
                        }
                        if (energy == "Low" && tension == "Moderate") {
                            binding.tvTraining.text = "Training Energizer"
                            binding.tvDescResultQuiz.text = "Congratulations! Based on your responses, you are well able to keep your tension in check. However, you’d perform more optimally if you are able to raise and maintain your energy level. Read on for recommendations"
                        }
                        if (energy == "Moderate" && tension == "Moderate") {
                            binding.tvTraining.text = "Training Energizer"
                            binding.tvDescResultQuiz.text = "Congratulations! Based on your responses, you are well able to keep your tension in check. However, you’d perform more optimally if you are able to raise and maintain your energy level. Read on for recommendations"
                        }
                        if (energy == "Low" && tension == "High") {
                            binding.tvTraining.text = "Training Relaxation"
                            binding.tvDescResultQuiz.text = "Based on your responses, learning to minimize tension is key to helping you unlock your best performance. Continuous and accumulated tension build-up can deplete our strength, weaken our will to meet the demands of your circumstances and diminish performance. Read on for recommendations"
                        }
                        if (energy == "Moderate" && tension == "High") {
                            binding.tvTraining.text = "Training Relaxation"
                            binding.tvDescResultQuiz.text = "Based on your responses, learning to minimize tension is key to helping you unlock your best performance. Continuous and accumulated tension build-up can deplete our strength, weaken our will to meet the demands of your circumstances and diminish performance. Read on for recommendations"
                        }
                        if (energy == "High" && tension == "High") {
                            binding.tvTraining.text = "Training Relaxation"
                            binding.tvDescResultQuiz.text = "Based on your responses, learning to minimize tension is key to helping you unlock your best performance. Continuous and accumulated tension build-up can deplete our strength, weaken our will to meet the demands of your circumstances and diminish performance. Read on for recommendations"
                        }
                        if (energy == "High" && tension == "Low") {
                            binding.tvTraining.text = "Training Maintenance"
                            binding.tvDescResultQuiz.text = "Congratulations! You are in an elite class. Based on your responses, you are well able to raise your level of energy to meet the demands of your circumstances while keeping your tension in check. Read on for recommendations"
                        }
                        if (energy == "High" && tension == "Moderate") {
                            binding.tvTraining.text = "Training Maintenance"
                            binding.tvDescResultQuiz.text = "Congratulations! You are in an elite class. Based on your responses, you are well able to raise your level of energy to meet the demands of your circumstances while keeping your tension in check. Read on for recommendations"
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        binding.btnRecomendation.setOnClickListener {
            resultQuiz = binding.tvTraining.text.toString()
            when {
                resultQuiz.equals("Training Energizer") -> {
                    val choose = Intent(this, RecommendEnergyQuizActivity::class.java)
                    choose.putExtra("result", resultQuiz)
                    startActivity(choose)
                    Animatoo.animateSlideUp(this)
                }
                resultQuiz.equals("Training Relaxation") -> {
                    val choose = Intent(this, RecommendEnergyQuizActivity::class.java)
                    choose.putExtra("result", resultQuiz)
                    startActivity(choose)
                    Animatoo.animateSlideUp(this)
                }
                resultQuiz.equals("Training Maintenance") -> {
                    val choose = Intent(this, RecommendEnergyQuizActivity::class.java)
                    choose.putExtra("result", resultQuiz)
                    startActivity(choose)
                    Animatoo.animateSlideUp(this)
                }
            }
        }
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