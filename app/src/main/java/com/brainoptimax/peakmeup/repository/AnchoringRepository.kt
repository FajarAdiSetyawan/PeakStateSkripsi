package com.brainoptimax.peakmeup.repository

import android.util.Log
import com.brainoptimax.peakmeup.model.anchoring.Anchoring
import com.brainoptimax.peakmeup.model.anchoring.Memory
import com.brainoptimax.peakmeup.model.anchoring.Resourceful
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AnchoringRepository(
    private val onRealtimeDbAddMemory: OnRealtimeDbAddMemory,
    private val onRealtimeDbAddResourceful: OnRealtimeDbAddResourceful,
    private val onRealtimeDbAddAnchoring: OnRealtimeDbAddAnchoring,
    private val onRealtimeDbAnchoring: OnRealtimeDbAnchoring,
    private val onRealtimeDbResourceful: OnRealtimeDbResourceful,
    private val onRealtimeDbMemory: OnRealtimeDbMemory
) {
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Anchoring")

    fun addMemory(uid: String, memoryText: String){
        val add = databaseReference.child(uid).child("Memory")
        val id = databaseReference.push().key
        val memoryAdd = Memory(id, memoryText)
        add.child(id!!).setValue(memoryAdd).addOnCompleteListener {
            if (it.isSuccessful){
                onRealtimeDbAddMemory.onSuccessAddMemory("success")
            }else{
                onRealtimeDbAddMemory.onFailureAddMemory(it.exception!!.message.toString())
            }
        }
    }

    fun addResourceful(uid: String, resourcefulText: String){
        val add = databaseReference.child(uid).child("Resourceful")
        val id = databaseReference.push().key
        val resourcefulAdd = Resourceful(id, resourcefulText)
        add.child(id!!).setValue(resourcefulAdd).addOnCompleteListener {
            if (it.isSuccessful){
                onRealtimeDbAddResourceful.onSuccessAddResourceful("success")
            }else{
                onRealtimeDbAddResourceful.onFailureAddResourceful(it.exception!!.message.toString())
            }
        }
    }

    fun addAnchoring(uid: String, resourceful: String, memory: String, note: String, currentDateTime: String){
        val add = databaseReference.child(uid).child("Result")
        val idAnchoring = databaseReference.push().key
        val anchoringAdd = Anchoring(idAnchoring, resourceful, memory, note, currentDateTime)
        add.child(idAnchoring!!).setValue(anchoringAdd).addOnCompleteListener {
            if (it.isSuccessful){
                onRealtimeDbAddAnchoring.onSuccessAddAnchoring("success")
            }else{
                onRealtimeDbAddAnchoring.onFailureAddAnchoring(it.exception!!.message.toString())
            }
        }
    }

    fun getResourceful(uid: String){
        databaseReference.child(uid).child("Resourceful").addValueEventListener(object : ValueEventListener {
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

    fun getMemory(uid: String){
        databaseReference.child(uid).child("Memory").addValueEventListener(object : ValueEventListener {
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

    fun getAnchoring(uid: String) {
        databaseReference.child(uid).child("Result").addValueEventListener(object : ValueEventListener {
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

    fun deleteResourceful(uid: String, id: String){
        databaseReference.child(uid).child("Resourceful").child(id).removeValue()
    }

    fun deleteMemory(uid: String, id: String){
        databaseReference.child(uid).child("Memory").child(id).removeValue()
    }

    fun deleteAnchoring(uid: String, id: String){
        databaseReference.child(uid).child("Result").child(id).removeValue()
    }

    interface OnRealtimeDbAddMemory {
        fun onSuccessAddMemory(status: String?)
        fun onFailureAddMemory(error: String?)
    }

    interface OnRealtimeDbAddResourceful {
        fun onSuccessAddResourceful(status: String?)
        fun onFailureAddResourceful(error: String?)
    }

    interface OnRealtimeDbAddAnchoring {
        fun onSuccessAddAnchoring(status: String?)
        fun onFailureAddAnchoring(error: String?)
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