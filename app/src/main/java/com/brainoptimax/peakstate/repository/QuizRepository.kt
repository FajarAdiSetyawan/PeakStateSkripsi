package com.brainoptimax.peakstate.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QuizRepository(
    private val onRealtimeDbPeak: OnRealtimeDbPeak,
    private val onRealtimeDbEnergy: OnRealtimeDbEnergy,
    private val onRealtimeDbTension: OnRealtimeDbTension,
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
            .child("Quiz")

    fun getResultEnergyQuiz(){
        databaseReference.child("Energy")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onRealtimeDbEnergy.onSuccessEnergyQuiz(snapshot.value.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbEnergy.onFailureEnergyQuiz(error)
                }
            })
    }

    fun getResultTensionQuiz(){
        databaseReference.child("Tension")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onRealtimeDbTension.onSuccessTensionQuiz(snapshot.value.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbTension.onFailureTensionQuiz(error)
                }
            })
    }

    fun getResultPeakQuiz(){
        databaseReference.child("PSR")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                   onRealtimeDbPeak.onSuccessPeakQuiz(snapshot.value.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbPeak.onFailurePeakQuiz(error)
                }
            })
    }

    interface OnRealtimeDbPeak {
        fun onSuccessPeakQuiz(peak: String?)
        fun onFailurePeakQuiz(error: DatabaseError?)
    }

    interface OnRealtimeDbEnergy {
        fun onSuccessEnergyQuiz(energy: String?)
        fun onFailureEnergyQuiz(error: DatabaseError?)
    }

    interface OnRealtimeDbTension {
        fun onSuccessTensionQuiz(tension: String?)
        fun onFailureTensionQuiz(error: DatabaseError?)
    }
}