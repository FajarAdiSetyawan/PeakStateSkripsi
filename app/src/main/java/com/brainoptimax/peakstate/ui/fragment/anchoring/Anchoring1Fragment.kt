package com.brainoptimax.peakstate.ui.fragment.anchoring

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.AnchoringAdapter
import com.brainoptimax.peakstate.databinding.FragmentAnchoring1Binding
import com.brainoptimax.peakstate.model.Resourceful
import com.brainoptimax.peakstate.ui.activity.anchoring.Anchoring1Activity
import com.brainoptimax.peakstate.ui.activity.anchoring.ResultAnchoringActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroAnchoringActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class Anchoring1Fragment : Fragment() {

    private var fragmentAnchoring1Binding: FragmentAnchoring1Binding? = null
    private val binding get() = fragmentAnchoring1Binding!!

    private lateinit var resourceful: ArrayList<Resourceful>

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAnchoring1Binding = FragmentAnchoring1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvAnchoring.layoutManager = layoutManager


        val base = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Resourceful")
        base.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    resourceful = arrayListOf()

                    for (item in dataSnapshot.children) {
                        val anchor = item.getValue(Resourceful::class.java)
                        resourceful.add(anchor!!)
                    }
                    binding.rvAnchoring.adapter = AnchoringAdapter(resourceful)
                }

            }
        })

        binding.ivAdd.setOnClickListener {
            addAnchoring()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAnchoring() {
        val anchoringText = binding.etAnchoring.text.toString()
        if (anchoringText.isEmpty()) {
            Toast.makeText(activity, resources.getString(R.string.anchoring_empty), Toast.LENGTH_SHORT)
                .show()
        } else {
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Resourceful").push()
                .setValue(
                    Resourceful(
                        anchoringText, Date().time
                    )
                ).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(
                        activity,
                        "Anchor Not Recorded",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


}