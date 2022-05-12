package com.brainoptimax.peakstate.ui.activity.anchoring

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAnchoring2Binding
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Anchoring2Activity : AppCompatActivity() {
    private var anchoring2Binding: ActivityAnchoring2Binding? = null
    private val binding get() = anchoring2Binding!!


    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        anchoring2Binding = ActivityAnchoring2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        val intent = intent
        val resourceful = intent.getStringExtra("RESOURCEFUL")

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference


        binding.btnNext.setOnClickListener {
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("tempAnchoring")
                .setValue(resourceful)
                .addOnSuccessListener {
                    startActivity(Intent(this, Anchoring3Activity::class.java))
                    Animatoo.animateSwipeLeft(this)
                    finish()
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