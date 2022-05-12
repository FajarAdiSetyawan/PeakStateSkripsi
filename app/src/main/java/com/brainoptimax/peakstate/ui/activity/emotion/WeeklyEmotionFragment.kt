package com.brainoptimax.peakstate.ui.activity.emotion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brainoptimax.peakstate.databinding.FragmentWeeklyEmotionBinding
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class WeeklyEmotionFragment : Fragment() {

    private var fragmentWeeklyEmotionBinding: FragmentWeeklyEmotionBinding? = null
    private val binding get() = fragmentWeeklyEmotionBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var countEmotion = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentWeeklyEmotionBinding = FragmentWeeklyEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding.tvMonthEmotion.text = currentDateandTime

        val refEmotion = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")

        refEmotion.child("total")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val total = snapshot.value.toString()
                        binding.tvTotalEmotion.text = "$total Emotions"
                    } else {
                        binding.tvTotalEmotion.text = "0 Emotions"
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })



        binding.cardWeek.setOnClickListener {
            val choose = Intent(activity!!, ResultEmotionActivity::class.java)
            startActivity(choose)
            Animatoo.animateSlideUp(activity!!)
        }
    }


}