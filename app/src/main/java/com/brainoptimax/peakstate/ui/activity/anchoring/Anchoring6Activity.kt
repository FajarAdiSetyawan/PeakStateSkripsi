package com.brainoptimax.peakstate.ui.activity.anchoring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAnchoring6Binding
import com.brainoptimax.peakstate.model.Anchoring
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Anchoring6Activity : AppCompatActivity() {
    private var anchoring6Binding: ActivityAnchoring6Binding? = null
    private val binding get() = anchoring6Binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        anchoring6Binding = ActivityAnchoring6Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, ResultAnchoringActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()

        }
    }


}