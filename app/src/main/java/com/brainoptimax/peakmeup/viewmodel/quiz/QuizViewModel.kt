package com.brainoptimax.peakmeup.viewmodel.quiz

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.model.Quiz
import com.brainoptimax.peakmeup.model.anchoring.Resourceful
import com.brainoptimax.peakmeup.repository.QuizRepository
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class QuizViewModel : ViewModel(),
    QuizRepository.OnRealtimeDbAddPeak,
    QuizRepository.OnRealtimeDbPeak,
    QuizRepository.OnRealtimeDbAddEnergy,
    QuizRepository.OnRealtimeDbEnergy,
    QuizRepository.OnRealtimeDbDeletePeak,
    QuizRepository.OnRealtimeDbDeleteEnergy
{
    val addEnergyQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddEnergy = MutableLiveData<String?>()

    val addPeakQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddPeak = MutableLiveData<String?>()

    val energyQuizMutableLiveData = MutableLiveData<List<Quiz>?>()
    val databaseErrorEnergy = MutableLiveData<DatabaseError?>()

    val peakQuizMutableLiveData = MutableLiveData<List<Quiz>?>()
    val databaseErrorPeak = MutableLiveData<DatabaseError?>()

    val deletePeakQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDeletePeak = MutableLiveData<String?>()

    val deleteEnergyQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDeleteEnergy = MutableLiveData<String?>()

    private lateinit var alertDialogLoading: AlertDialog

    private val quizRepository: QuizRepository = QuizRepository(this, this, this, this, this, this)

    fun getResultPeak(uid: String){
        quizRepository.getResultPeakQuiz(uid)
    }

    fun getResultEnergy(uid: String){
        quizRepository.getResultEnergyQuiz(uid)
    }

    fun deleteEnergyQuiz(uid: String, idQuiz: String){
        quizRepository.deleteEnergyQuiz(uid, idQuiz)
    }

    fun deletePeakQuiz(uid: String, idQuiz: String){
        quizRepository.deletePeakQuiz(uid, idQuiz)
    }

    fun savePeakQuiz(
        uid: String,
        peak: String?
    ){
        quizRepository.addPeakQuiz(uid, peak!!)
    }

    fun saveEnergyQuiz(
        uid: String,
        energy: String?,
        tension: String?
    ){
        quizRepository.addEnergyQuiz(uid, energy!!, tension!!)
    }



    fun openLoadingDialog(fragmentActivity: FragmentActivity) {
        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading,null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        alertDialogLoading = progress.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.setAnimation(R.raw.loading_orange)
        lottie.playAnimation()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = fragmentActivity.resources.getString(R.string.save_quiz)
        alertDialogLoading.show()
    }

    fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }

    override fun onSuccessPeakQuiz(peak: List<Quiz>?) {
        peakQuizMutableLiveData.value = peak
    }

    override fun onFailurePeakQuiz(error: DatabaseError?) {
        databaseErrorPeak.value = error
    }

    override fun onSuccessEnergyQuiz(energy: List<Quiz>?) {
        energyQuizMutableLiveData.value = energy
    }

    override fun onFailureEnergyQuiz(error: DatabaseError?) {
        databaseErrorEnergy.value = error
    }

    override fun onSuccessAddPeakQuiz(status: String?) {
        addPeakQuizMutableLiveData.value = status
    }

    override fun onFailureAddPeakQuiz(error: String?) {
        databaseErrorAddPeak.value = error
    }

    override fun onSuccessAddEnergyQuiz(status: String?) {
        addEnergyQuizMutableLiveData.value = status
    }

    override fun onFailureAddEnergyQuiz(error: String?) {
        databaseErrorAddEnergy.value = error
    }

    override fun onSuccessDeleteEnergy(status: String?) {
        deleteEnergyQuizMutableLiveData.value = status
    }

    override fun onFailureDeleteEnergy(error: String?) {
        databaseErrorDeleteEnergy.value = error
    }

    override fun onSuccessDeletePeak(status: String?) {
        deletePeakQuizMutableLiveData.value = status
    }

    override fun onFailureDeletePeak(error: String?) {
        databaseErrorDeletePeak.value = error
    }


}