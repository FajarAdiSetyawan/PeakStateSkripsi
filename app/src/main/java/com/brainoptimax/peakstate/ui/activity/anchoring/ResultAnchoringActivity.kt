package com.brainoptimax.peakstate.ui.activity.anchoring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityResultAnchoringBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultAnchoringActivity : AppCompatActivity() {

    private var activityResultAnchoringBinding: ActivityResultAnchoringBinding? = null
    private val binding get() = activityResultAnchoringBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultAnchoringBinding = ActivityResultAnchoringBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        showLoading()
        databaseReference.child("Users").child(auth.currentUser!!.uid).child("Anchoring")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    hideLoading()
                    if (dataSnapshot.exists()) {
                        val resourceful = dataSnapshot.child("resourceful").value.toString()
                        val descMemory = dataSnapshot.child("descMemory").value.toString()
                        val memory = dataSnapshot.child("memory").value.toString()
                        val dateTime = dataSnapshot.child("dateTime").value.toString()

                        binding.tvDateTime.text = dateTime
                        binding.tvResult.text = resourceful
                        binding.tvMemory.text = memory
                        binding.tvDescResultQuiz.text = descMemory

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        binding.btnDone.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateSwipeRight(this)
            finish()
        }
    }


    private fun showLoading(){
        binding.shimmerDescResult.visibility = View.VISIBLE
        binding.shimmerDescResult.startShimmer()
        binding.shimmerMemory.visibility = View.VISIBLE
        binding.shimmerMemory.startShimmer()
        binding.shimmerResult.visibility = View.VISIBLE
        binding.shimmerResult.startShimmer()
        binding.tvResult.visibility = View.GONE
        binding.tvDescResultQuiz.visibility = View.GONE
        binding.tvMemory.visibility = View.GONE
        binding.tvDateTime.visibility = View.GONE
    }

    private fun hideLoading(){
        binding.shimmerResult.visibility = View.GONE
        binding.shimmerResult.stopShimmer()
        binding.shimmerMemory.visibility = View.GONE
        binding.shimmerMemory.stopShimmer()
        binding.shimmerDescResult.visibility = View.GONE
        binding.shimmerDescResult.stopShimmer()
        binding.tvResult.visibility = View.VISIBLE
        binding.tvDescResultQuiz.visibility = View.VISIBLE
        binding.tvMemory.visibility = View.VISIBLE
        binding.tvDateTime.visibility = View.VISIBLE
    }
}