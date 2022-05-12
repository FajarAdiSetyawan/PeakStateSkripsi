package com.brainoptimax.peakstate.ui.activity.anchoring

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.AnchoringAdapter
import com.brainoptimax.peakstate.databinding.ActivityAnchoring1Binding
import com.brainoptimax.peakstate.model.Resourceful
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class Anchoring1Activity : AppCompatActivity() {
    private var anchoring1Binding: ActivityAnchoring1Binding? = null
    private val binding get() = anchoring1Binding!!

    private lateinit var resourceful: ArrayList<Resourceful>
    private lateinit var anchoringAdapter: AnchoringAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        anchoring1Binding = ActivityAnchoring1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvAnchoring.layoutManager = layoutManager


        val base =
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Resourceful")
        base.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    resourceful = arrayListOf()

                    for (item in dataSnapshot.children) {
                        val anchor = item.getValue(Resourceful::class.java)
                        this@Anchoring1Activity.resourceful.add(anchor!!)
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
            Toast.makeText(this, resources.getString(R.string.anchoring_empty), Toast.LENGTH_SHORT)
                .show()
        } else {
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Resourceful")
                .push()
                .setValue(
                    Resourceful(
                        anchoringText, Date().time
                    )
                ).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Anchor Not Recorded",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


}

