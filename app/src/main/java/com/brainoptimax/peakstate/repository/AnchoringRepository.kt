package com.brainoptimax.peakstate.repository

import android.util.Log
import com.brainoptimax.peakstate.model.anchoring.Anchoring
import com.brainoptimax.peakstate.model.anchoring.Memory
import com.brainoptimax.peakstate.model.anchoring.Resourceful
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AnchoringRepository(
    private val onRealtimeDbAnchoring: OnRealtimeDbAnchoring,
    private val onRealtimeDbResourceful: OnRealtimeDbResourceful,
    private val onRealtimeDbMemory: OnRealtimeDbMemory
) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid).child("Anchoring")

    fun getResourceful(){
        databaseReference.child("Resourceful").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val resourceful: MutableList<Resourceful> = ArrayList()

                for (item in snapshot.children) {
                    val resourcefulList = item.getValue(Resourceful::class.java)
                    resourceful.add(resourcefulList!!)
                }
                Log.d("TAG", "onDataChangeResourceful: $resourceful")
                onRealtimeDbResourceful.onSuccessResourceful(resourceful)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbResourceful.onFailureResourceful(error)
            }
        })
    }

    fun getMemory(){
        databaseReference.child("Memory").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val memory: MutableList<Memory> = ArrayList()

                for (item in snapshot.children) {
                    val memoryList = item.getValue(Memory::class.java)
                    memory.add(memoryList!!)
                }
                Log.d("TAG", "onDataChangeMemory: $memory")
                onRealtimeDbMemory.onSuccessMemory(memory)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbMemory.onFailureMemory(error)
            }
        })
    }

    fun getAnchoring() {
        databaseReference.child("Result").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val anchoring: MutableList<Anchoring> = ArrayList()

                for (item in snapshot.children) {
                    val anchoringList = item.getValue(Anchoring::class.java)
                    anchoring.add(anchoringList!!)
                }
                Log.d("TAG", "onDataChangeAnchoring: $anchoring")
                onRealtimeDbAnchoring.onSuccess(anchoring)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbAnchoring.onFailure(error)
            }
        })
    }

    fun deleteResourceful(id: String){
        databaseReference.child("Resourceful").child(id).removeValue()
    }

    fun deleteMemory(id: String){
        databaseReference.child("Memory").child(id).removeValue()
    }

    fun deleteAnchoring(id: String){
        databaseReference.child("Result").child(id).removeValue()
    }

    interface OnRealtimeDbAnchoring {
        fun onSuccess(anchoring: List<Anchoring>?)
        fun onFailure(error: DatabaseError?)
    }

    interface OnRealtimeDbResourceful {
        fun onSuccessResourceful(resourceful: List<Resourceful>?)
        fun onFailureResourceful(error: DatabaseError?)
    }

    interface OnRealtimeDbMemory {
        fun onSuccessMemory(memory: List<Memory>?)
        fun onFailureMemory(error: DatabaseError?)
    }
}