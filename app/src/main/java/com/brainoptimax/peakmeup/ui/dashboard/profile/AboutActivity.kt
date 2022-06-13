package com.brainoptimax.peakmeup.ui.dashboard.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brainoptimax.peakmeup.ui.MainActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private var activityAboutBinding: ActivityAboutBinding? = null
    private val binding get() = activityAboutBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAboutBinding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)) // pindah ke login
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        binding.btnAboutDialog.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q="+ -6.149464136156816 + "," + 106.89091201349316)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}