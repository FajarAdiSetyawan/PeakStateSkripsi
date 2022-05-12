package com.brainoptimax.peakstate.ui.activity.quiz

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
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityResultPsrQuizBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultPSRQuizActivity : AppCompatActivity() {

    private var activityResultPsrQuizBinding: ActivityResultPsrQuizBinding? = null
    private val binding get() = activityResultPsrQuizBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private val networkMonitor = NetworkMonitorUtil(this)

    private  var resultQuiz: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultPsrQuizBinding = ActivityResultPsrQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_tosca)
        checkConnection()
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference


        showLoading()

        databaseReference.child("Users").child(auth.currentUser!!.uid).child("PSR")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoading()

                    if (snapshot.exists()) {
                        val result = snapshot.value.toString()
                        binding.tvResult.text = "$result Readiness"
                        if (result == "Low") {
                            resultQuiz = "Low"
                            binding.tvDescResultQuiz.text = "Your profile suggests that your readiness for attaining peak state can be significantly improved. None of us were given a user’s guide at birth to fully avail of the resources of our mind and body we need to surmount the challenges and navigate the difficult terrains of life. The good news is that by making a commitment to swiftly adopt and practice new routines, develop new habits suggested here, you can experience an elevation in your peak state score and live a fulfilling life. Keep in mind that the key to change is consistency and the daily, proper performance of practices and routines."
                        }
                        if (result == "Medium") {
                            resultQuiz = "Medium"
                            binding.tvDescResultQuiz.text = "Your profile suggests that you have room for improvement in your readiness score in order to attain and access peak state more consistently. To elevate your peak state score, make a commitment to adopting and practising proper routines suggested here and develop new habits swiftly. Keep in mind that the key to change is the right mindset and consistency in performing readiness practices and routines regularly."
                        }
                        if (result == "High") {
                            resultQuiz = "High"
                            binding.tvDescResultQuiz.text = "Congratulations! Your profile suggests that you have high readiness for accessing the peak state. To improve and maintain your readiness for peak state, make a commitment to adopting and practising proper routines suggested here and develop new habits.Keep in mind that the key to change is the right mindset and consistency in performing readiness practices and routines regularly."
                        }
                    } else {
                        binding.tvResult.text = "Null"
                        binding.tvDescResultQuiz.text = "Null"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.tvResult.text = "Null"
                    binding.tvDescResultQuiz.text = "Null"
                }
            })


        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }

        binding.btnRecomendation.setOnClickListener {
            when {
                resultQuiz.equals("Low") -> {
                    val choose = Intent(this, RecommendPSRQuizActivity::class.java)
                    choose.putExtra("result", resultQuiz)
                    startActivity(choose)
                    Animatoo.animateSlideUp(this)
                }
                resultQuiz.equals("Medium") -> {
                    val choose = Intent(this, RecommendPSRQuizActivity::class.java)
                    choose.putExtra("result", resultQuiz)
                    startActivity(choose)
                    Animatoo.animateSlideUp(this)
                }
                resultQuiz.equals("High") -> {
                    val choose = Intent(this, RecommendPSRQuizActivity::class.java)
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