package com.brainoptimax.peakmeup.viewmodel.emotion

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainoptimax.peakmeup.model.Emotion
import com.brainoptimax.peakmeup.repository.EmotionRepository
import com.google.firebase.database.DatabaseError

class EmotionViewModel : ViewModel(),
    EmotionRepository.OnRealtimeAddDbEmotion,
    EmotionRepository.OnRealtimeDbEmotion,
    EmotionRepository.OnRealtimeDbPositive,
    EmotionRepository.OnRealtimeDbNegative,
    EmotionRepository.OnRealtimeDbTotalAllEmotion,
    EmotionRepository.OnRealtimeDbTotalPerEmotion {

    private val emotionRepository: EmotionRepository =
        EmotionRepository(this, this, this, this, this, this)

    val emotionAddMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddEmotions = MutableLiveData<String?>()

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

    fun allEmotions(uid: String, day: String) {
        emotionRepository.getAllEmotion(uid, day)
    }

    fun allPositive(uid: String) {
        emotionRepository.getEmotionPositive(uid)
    }

    fun allNegative(uid: String) {
        emotionRepository.getEmotionNegative(uid)
    }

    fun totalAllEmotion(uid: String) {
        emotionRepository.getTotalAllEmotions(uid)
    }

    fun totalPerEmotion(uid: String, condition: String, emotion: String) {
        emotionRepository.getTotalPerEmotions(uid, condition, emotion)
    }

    fun addEmotion(
        uidUser: String,
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
        emotionRepository.addAllEmotion(
            uidUser,
            totalAllEmotion,
            totalPerEmotion,
            condition,
            emotionSelected,
            emotionNote,
            currentDateTime,
            dateFormat,
            timeFormat
        )
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

    override fun onSuccessAddEmotion(emotion: String?) {
        emotionAddMutableLiveData.value = emotion.toString()
    }

    override fun onFailureAddEmotion(error: String?) {
        databaseErrorAddEmotions.value = error
    }

}