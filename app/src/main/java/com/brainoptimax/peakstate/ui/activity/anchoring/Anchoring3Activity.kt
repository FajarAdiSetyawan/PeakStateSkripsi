package com.brainoptimax.peakstate.ui.activity.anchoring

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.MemoryAdapter
import com.brainoptimax.peakstate.databinding.ActivityAnchoring3Binding
import com.brainoptimax.peakstate.model.Memory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class Anchoring3Activity : AppCompatActivity() {
    private var anchoring3Binding: ActivityAnchoring3Binding? = null
    private val binding get() = anchoring3Binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var memory: ArrayList<Memory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        anchoring3Binding = ActivityAnchoring3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.cyan_primary)

        val intent = intent
        val anchoring = intent.getStringExtra("ANCHORING")
        binding.tvAnchor.text = anchoring

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvMemory.layoutManager = layoutManager

        binding.ivAdd.setOnClickListener {
            addMemory()
        }

        val base = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Memory")
        base.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    memory = arrayListOf()

                    for (item in dataSnapshot.children) {
                        val memoryList = item.getValue(Memory::class.java)
                        memory.add(memoryList!!)
                    }
                    binding.rvMemory.adapter = MemoryAdapter(memory)
                }

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMemory() {
        val memoryText = binding.etMemory.text.toString()
        if (memoryText.isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.memory_empty), Toast.LENGTH_SHORT)
                .show()
        } else {
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Memory").push()
                .setValue(
                    Memory(
                        memoryText, Date().time
                    )
                ).addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Memory Not Recorded",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}