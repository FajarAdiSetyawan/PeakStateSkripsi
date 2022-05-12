package com.brainoptimax.peakstate.ui.activity.anchoring

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityAnchoring4Binding
import com.brainoptimax.peakstate.ui.activity.reminders.DetailReminderActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Anchoring4Activity : AppCompatActivity() {
    private var anchoring4Binding: ActivityAnchoring4Binding? = null
    private val binding get() = anchoring4Binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        anchoring4Binding = ActivityAnchoring4Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val intent = intent
        val memory = intent.getStringExtra("MEMORY")

        binding.tvMemory.text = memory

        binding.btnNext.setOnClickListener {
            val descMemory = binding.etMemory.text.toString().trim()
            when {
                descMemory.isEmpty() -> {
                    Toast.makeText(this, "Description Memory Not Blank", Toast.LENGTH_SHORT).show()
                }
                else -> {


                    val intent = Intent(this, Anchoring5Activity::class.java)
                    intent.putExtra(Anchoring5Activity.EXTRA_MEMORY, memory)
                    intent.putExtra(Anchoring5Activity.EXTRA_DESC, descMemory)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                }
            }
        }
    }
}