package com.brainoptimax.peakmeup.repository

import android.util.Log
import com.brainoptimax.peakmeup.model.Emotion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class EmotionRepository(
    private val onRealtimeDbEmotion: OnRealtimeDbEmotion,
    private val onRealtimeDbPositive: OnRealtimeDbPositive,
    private val onRealtimeDbNegative: OnRealtimeDbNegative,
    private val onRealtimeDbTotalAllEmotion: OnRealtimeDbTotalAllEmotion,
    private val onRealtimeDbTotalPerEmotion: OnRealtimeDbTotalPerEmotion,

    ) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
            .child("Emotion")


    fun getAllEmotion(day: String) {
        databaseReference.child("daily").child(day)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val emotion: MutableList<Emotion> = ArrayList()

                    for (item in dataSnapshot.children) {
                        val emotionList = item.getValue(Emotion::class.java)
                        emotion.add(emotionList!!)
                    }
                    Log.d("TAG", "onDataChangeTodo: $emotion")
                    onRealtimeDbEmotion.onSuccessEmotion(emotion)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onRealtimeDbEmotion.onFailureEmotion(databaseError)
                }
            })
    }

    fun getTotalAllEmotions() {
        databaseReference.child("totalAllEmotion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onRealtimeDbTotalAllEmotion.onSuccessEmotionTotalAllEmotion(snapshot.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbTotalAllEmotion.onFailureEmotionTotalAllEmotion(error)
                }
            })
    }

    fun getTotalPerEmotions(condition: String, emotion: String) {
        databaseReference.child("EmotionName").child(condition).child(emotion)
            .child("totalPerEmotion").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onRealtimeDbTotalPerEmotion.onSuccessEmotionTotalPerEmotion(snapshot.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbTotalPerEmotion.onFailureEmotionTotalPerEmotion(error)
                }
            })
    }

    fun getEmotionPositive() {
        databaseReference.child("EmotionName").child("Positive").get()
            .addOnCompleteListener { task ->
                val emotion: MutableList<Emotion> = ArrayList()

                if (task.isSuccessful) {
                    for (ds in task.result.children) {
                        val emotionList = ds.getValue(Emotion::class.java)
                        emotion.add(emotionList!!)
                    }
                    Log.d("TAG", "onDataPositive: $emotion")
                    onRealtimeDbPositive.onSuccessEmotionPositive(emotion)
                } else {
                    Log.d(
                        "TAG",
                        task.exception!!.message.toString()
                    ) //Don't ignore potential errors!
                    onRealtimeDbPositive.onFailureEmotionPositive(task.exception!!.message.toString())
                }
            }
    }

    fun getEmotionNegative() {
        databaseReference.child("EmotionName").child("Negative").get()
            .addOnCompleteListener { task ->
                val emotion: MutableList<Emotion> = ArrayList()

                if (task.isSuccessful) {
                    for (ds in task.result.children) {
                        val emotionList = ds.getValue(Emotion::class.java)
                        emotion.add(emotionList!!)
                    }
                    Log.d("TAG", "onDataNegative: $emotion")
                    onRealtimeDbNegative.onSuccessEmotionNegative(emotion)
                } else {
                    Log.d(
                        "TAG",
                        task.exception!!.message.toString()
                    ) //Don't ignore potential errors!
                    onRealtimeDbNegative.onFailureEmotionNegative(task.exception!!.message.toString())
                }
            }
    }

    interface OnRealtimeDbEmotion {
        fun onSuccessEmotion(emotion: List<Emotion>?)
        fun onFailureEmotion(error: DatabaseError?)
    }

    interface OnRealtimeDbPositive {
        fun onSuccessEmotionPositive(emotion: List<Emotion>?)
        fun onFailureEmotionPositive(error: String?)
    }

    interface OnRealtimeDbNegative {
        fun onSuccessEmotionNegative(emotion: List<Emotion>?)
        fun onFailureEmotionNegative(error: String?)
    }

    interface OnRealtimeDbTotalAllEmotion {
        fun onSuccessEmotionTotalAllEmotion(emotion: String?)
        fun onFailureEmotionTotalAllEmotion(error: DatabaseError?)
    }

    interface OnRealtimeDbTotalPerEmotion {
        fun onSuccessEmotionTotalPerEmotion(emotion: String?)
        fun onFailureEmotionTotalPerEmotion(error: DatabaseError?)
    }


}