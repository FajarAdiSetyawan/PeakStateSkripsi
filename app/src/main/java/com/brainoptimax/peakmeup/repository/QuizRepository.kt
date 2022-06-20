package com.brainoptimax.peakmeup.repository

import android.util.Log
import com.brainoptimax.peakmeup.model.Quiz
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class QuizRepository(
    private val onRealtimeDbAddPeak: OnRealtimeDbAddPeak,
    private val onRealtimeDbPeak: OnRealtimeDbPeak,
    private val onRealtimeDbAddEnergy: OnRealtimeDbAddEnergy,
    private val onRealtimeDbEnergy: OnRealtimeDbEnergy,
    private val onRealtimeDbDeleteEnergy: OnRealtimeDbDeleteEnergy,
    private val onRealtimeDbDeletePeak: OnRealtimeDbDeletePeak,
) {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Quiz")

    fun addPeakQuiz(uidUser: String, result: String){
        val sdf = SimpleDateFormat("E, d MMMM yyyy - H:mm ", Locale.getDefault())
        val currentDateTime = sdf.format(Date())

        val add = databaseReference.child(uidUser).child("PSR")
        val idQuiz = databaseReference.push().key
        val quizAdd = Quiz(idQuiz, result, currentDateTime)
        add.child(idQuiz!!).setValue(quizAdd).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbAddPeak.onSuccessAddPeakQuiz("success")
            } else {
                onRealtimeDbAddPeak.onFailureAddPeakQuiz(it.exception!!.message.toString())
            }
        }
    }

    fun addEnergyQuiz(uidUser: String, energy: String, tension: String?){
        val sdf = SimpleDateFormat("E, d MMMM yyyy - H:mm ", Locale.getDefault())
        val currentDateTime = sdf.format(Date())

        val add = databaseReference.child(uidUser).child("Energy")
        val idQuiz = databaseReference.push().key
        val quizAdd = Quiz(idQuiz, energy, tension, currentDateTime)
        add.child(idQuiz!!).setValue(quizAdd).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbAddEnergy.onSuccessAddEnergyQuiz("success")
            } else {
                onRealtimeDbAddEnergy.onFailureAddEnergyQuiz(it.exception!!.message.toString())
            }
        }
    }

    fun getResultEnergyQuiz(uidUser: String){
        databaseReference.child(uidUser).child("Energy").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quiz: MutableList<Quiz> = ArrayList()

                for (item in snapshot.children) {
                    val energyList = item.getValue(Quiz::class.java)
                    quiz.add(energyList!!)
                }
                Log.d("TAG", "onDataChangeEnergyQuiz: $quiz")
                onRealtimeDbEnergy.onSuccessEnergyQuiz(quiz)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbEnergy.onFailureEnergyQuiz(error)
            }
        })
    }

    fun getResultPeakQuiz(uidUser: String){
        databaseReference.child(uidUser).child("PSR").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quiz: MutableList<Quiz> = ArrayList()

                for (item in snapshot.children) {
                    val peakList = item.getValue(Quiz::class.java)
                    quiz.add(peakList!!)
                }
                Log.d("TAG", "onDataChangeEnergyQuiz: $quiz")
                onRealtimeDbPeak.onSuccessPeakQuiz(quiz)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbPeak.onFailurePeakQuiz(error)
            }
        })
    }


    fun deleteEnergyQuiz(uidUser: String, idQuiz: String){
        databaseReference.child(uidUser).child("Energy").child(idQuiz).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbDeleteEnergy.onSuccessDeleteEnergy("success")
            } else {
                onRealtimeDbDeleteEnergy.onFailureDeleteEnergy(it.exception!!.message.toString())
            }
        }
    }

    fun deletePeakQuiz(uidUser: String, idQuiz: String){
        databaseReference.child(uidUser).child("PSR").child(idQuiz).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbDeletePeak.onSuccessDeletePeak("success")
            } else {
                onRealtimeDbDeletePeak.onFailureDeletePeak(it.exception!!.message.toString())
            }
        }
    }

    interface OnRealtimeDbAddPeak {
        fun onSuccessAddPeakQuiz(status: String?)
        fun onFailureAddPeakQuiz(error: String?)
    }

    interface OnRealtimeDbPeak {
        fun onSuccessPeakQuiz(peak: List<Quiz>?)
        fun onFailurePeakQuiz(error: DatabaseError?)
    }

    interface OnRealtimeDbEnergy {
        fun onSuccessEnergyQuiz(energy: List<Quiz>?)
        fun onFailureEnergyQuiz(error: DatabaseError?)
    }

    interface OnRealtimeDbAddEnergy {
        fun onSuccessAddEnergyQuiz(status: String?)
        fun onFailureAddEnergyQuiz(error: String?)
    }

    interface OnRealtimeDbDeleteEnergy {
        fun onSuccessDeleteEnergy(status: String?)
        fun onFailureDeleteEnergy(error: String?)
    }

    interface OnRealtimeDbDeletePeak {
        fun onSuccessDeletePeak(status: String?)
        fun onFailureDeletePeak(error: String?)
    }
}