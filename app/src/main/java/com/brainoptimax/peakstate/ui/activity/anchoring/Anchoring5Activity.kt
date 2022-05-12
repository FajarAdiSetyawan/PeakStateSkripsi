package com.brainoptimax.peakstate.ui.activity.anchoring

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAnchoring5Binding
import com.brainoptimax.peakstate.model.Anchoring
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import render.animations.Render
import render.animations.Slide
import java.text.SimpleDateFormat
import java.util.*

class Anchoring5Activity : AppCompatActivity() {
    private var anchoring5Binding: ActivityAnchoring5Binding? = null
    private val binding get() = anchoring5Binding!!

    companion object {
        const val EXTRA_MEMORY = "extra_memory"
        const val EXTRA_DESC = "extra_desc"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private val animation = Render(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        anchoring5Binding = ActivityAnchoring5Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val memory = intent.getStringExtra(EXTRA_MEMORY)
        val desc = intent.getStringExtra(EXTRA_DESC)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Users").child(auth.currentUser!!.uid).child("tempAnchoring")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val resourceful = snapshot.value.toString()
                        binding.tvReso.text = resourceful
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        binding.btnStart.setOnClickListener {
            animation.setAnimation(Slide.OutDown(binding.btnStart))
            animation.start()
            binding.btnStart.visibility = View.INVISIBLE

            object : CountDownTimer(15000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvTimer.text = (millisUntilFinished / 1000 + 1).toString() + "  " + resources.getString(
                        R.string.seconds)
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    binding.tvTimer.text = "0  " + resources.getString(R.string.seconds)
                    binding.btnDone.visibility = View.VISIBLE
                    animation.setAnimation(Slide.InUp(binding.btnDone))
                    animation.start()
                }
            }.start()
        }

        binding.btnDone.setOnClickListener {
            val sdf = SimpleDateFormat("E, d MMMM yyyy - H:mm ", Locale.getDefault())
            val currentDateAndTime = sdf.format(Date())
            val reso = binding.tvReso.text.toString().trim()

            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Anchoring")
                .setValue(Anchoring(reso, memory, desc, currentDateAndTime))
                .addOnSuccessListener {
                    val intent =
                        Intent(this, Anchoring6Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                    Toast.makeText(
                        this,
                        "Anchoring Recorded",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Anchoring Not Recorded",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}