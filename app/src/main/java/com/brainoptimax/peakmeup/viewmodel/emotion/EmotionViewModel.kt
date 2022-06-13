package com.brainoptimax.peakmeup.viewmodel.emotion

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainoptimax.peakmeup.model.Emotion
import com.brainoptimax.peakmeup.repository.EmotionRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class EmotionViewModel : ViewModel(),
    EmotionRepository.OnRealtimeDbEmotion,
    EmotionRepository.OnRealtimeDbPositive,
    EmotionRepository.OnRealtimeDbNegative,
    EmotionRepository.OnRealtimeDbTotalAllEmotion,
    EmotionRepository.OnRealtimeDbTotalPerEmotion {

    private val emotionRepository: EmotionRepository =
        EmotionRepository(this, this, this, this, this)

    val emotionMutableLiveData = MutableLiveData<List<Emotion>?>()
    val databaseErrorEmotions = MutableLiveData<DatabaseError?>()

    val positiveMutableLiveData = MutableLiveData<List<Emotion>?>()
    val databaseErrorPositive = MutableLiveData<String?>()

    val negativeMutableLiveData = MutableLiveData<List<Emotion>?>()
    val databaseErrorNegative = MutableLiveData<String?>()

    val totalAllEmotionMutableLiveData = MutableLiveData<String?>()
    private val databaseErrorTotalAllEmotion = MutableLiveData<DatabaseError?>()

    val totalPerEmotionMutableLiveData = MutableLiveData<String?>()
    private val databaseErrorTotalPerEmotion = MutableLiveData<DatabaseError?>()

    var status = MutableLiveData<Boolean?>()

    fun allEmotions(day: String) {
        emotionRepository.getAllEmotion(day)
    }

    val allPositive: Unit
        get() {
            emotionRepository.getEmotionPositive()
        }

    val allNegative: Unit
        get() {
            emotionRepository.getEmotionNegative()
        }

    val totalAllEmotion: Unit
        get() {
            emotionRepository.getTotalAllEmotions()
        }


    fun totalPerEmotion(condition: String, emotion: String) {
        emotionRepository.getTotalPerEmotions(condition, emotion)
    }

    fun addEmotion(
        ref: DatabaseReference,
        view: View?,
        totalAllEmotion: Int,
        totalPerEmotion: Int,
        condition: String,
        emotionSelected: String,
        emotionNote: String,
        currentDateTime: String,
        dateFormat: String,
        timeFormat: String
    ) {
        ref.child("totalAllEmotion").setValue(totalAllEmotion)
        ref.child("EmotionName").child(condition).child(emotionSelected)
            .setValue(Emotion(emotionSelected, totalAllEmotion, totalPerEmotion))
        ref.child("daily").child(dateFormat).child(timeFormat)
            .setValue(Emotion(emotionSelected, emotionNote, currentDateTime))
            .addOnCompleteListener {
                // jika berhasil disimpan
                if (it.isSuccessful) {
                    status.value = true
                } else {
                    // tampilkan dialog error
                    val message: String =
                        it.exception!!.message.toString() // mengambil pesan error
                    Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }

    override fun onSuccessEmotion(emotion: List<Emotion>?) {
        emotionMutableLiveData.value = emotion
    }

    override fun onFailureEmotion(error: DatabaseError?) {
        databaseErrorEmotions.value = error
    }

    override fun onSuccessEmotionPositive(emotion: List<Emotion>?) {
        positiveMutableLiveData.value = emotion
    }

    override fun onFailureEmotionPositive(error: String?) {
        databaseErrorPositive.value = error
    }

    override fun onSuccessEmotionNegative(emotion: List<Emotion>?) {
        negativeMutableLiveData.value = emotion
    }

    override fun onFailureEmotionNegative(error: String?) {
        databaseErrorNegative.value = error
    }

    override fun onSuccessEmotionTotalAllEmotion(emotion: String?) {
        totalAllEmotionMutableLiveData.value = emotion
    }

    override fun onFailureEmotionTotalAllEmotion(error: DatabaseError?) {
        databaseErrorTotalAllEmotion.value = error
    }

    override fun onSuccessEmotionTotalPerEmotion(emotion: String?) {
        totalPerEmotionMutableLiveData.value = emotion
    }

    override fun onFailureEmotionTotalPerEmotion(error: DatabaseError?) {
        databaseErrorTotalPerEmotion.value = error
    }

}