package com.brainoptimax.peakstate.ui.activity.emotion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.adapter.emotions.ResultPositiveAdapter
import com.brainoptimax.peakstate.databinding.FragmentResultPositiveBinding
import com.brainoptimax.peakstate.model.Emotion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class ResultPositiveFragment : Fragment() {

    private var fragmentResultPositiveBinding: FragmentResultPositiveBinding? = null
    private val binding get() = fragmentResultPositiveBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var emotions: ArrayList<Emotion>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultPositiveBinding = FragmentResultPositiveBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rvEmotionsPositive.layoutManager = layoutManager

        showLoading()
        val refEmotion = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")
        refEmotion.child("EmotionName").child("Positive")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    when {
                        dataSnapshot.exists() -> {
                            goneLoading()
                            binding.layoutEmpty.visibility = View.INVISIBLE

                            emotions = arrayListOf()

                            for (item in dataSnapshot.children) {
                                val emotionList = item.getValue(Emotion::class.java)
                                emotions.add(emotionList!!)
                            }
                            binding.rvEmotionsPositive.adapter = ResultPositiveAdapter(emotions)
                        }
                        else -> {
                            binding.layoutEmpty.visibility = View.VISIBLE
                            binding.rvEmotionsPositive.visibility = View.INVISIBLE
                            binding.pbEmotions.visibility = View.GONE
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


//
    }

    private fun showLoading(){
        binding.pbEmotions.visibility = View.VISIBLE
        binding.rvEmotionsPositive.visibility = View.INVISIBLE
    }

    private fun goneLoading(){
        binding.pbEmotions.visibility = View.INVISIBLE
        binding.rvEmotionsPositive.visibility = View.VISIBLE
    }


}