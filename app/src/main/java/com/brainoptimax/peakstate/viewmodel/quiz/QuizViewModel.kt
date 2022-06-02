package com.brainoptimax.peakstate.viewmodel.quiz

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
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.repository.QuizRepository
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class QuizViewModel : ViewModel(),
    QuizRepository.OnRealtimeDbPeak,
    QuizRepository.OnRealtimeDbEnergy,
        QuizRepository.OnRealtimeDbTension
{
    val energyQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorEnergy = MutableLiveData<DatabaseError?>()

    val tensionQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorTension = MutableLiveData<DatabaseError?>()

    val peakQuizMutableLiveData = MutableLiveData<String?>()
    val databaseErrorPeak = MutableLiveData<DatabaseError?>()

    private lateinit var alertDialogLoading: AlertDialog

    private val quizRepository: QuizRepository = QuizRepository(this, this, this)

    val peakQuizResult: Unit
        get() {
            quizRepository.getResultPeakQuiz()
        }

    val energyQuizResult: Unit
        get() {
            quizRepository.getResultEnergyQuiz()
        }

    val tensionQuizResult: Unit
        get() {
            quizRepository.getResultTensionQuiz()
        }

    fun saveEnergyQuiz(
        ref: DatabaseReference,
        energy: String?,
        tension: String?
    ){
        ref.child("Energy").setValue(energy).addOnCompleteListener {
            closeLoadingDialog()
        }
        ref.child("Tension").setValue(tension).addOnCompleteListener {
            closeLoadingDialog()
        }
    }

    fun savePeakQuiz(
        ref: DatabaseReference,
        peak: String?
    ){
        ref.child("PSR").setValue(peak).addOnCompleteListener {
            closeLoadingDialog()
        }
    }

    override fun onSuccessPeakQuiz(peak: String?) {
        peakQuizMutableLiveData.value = peak
    }

    override fun onFailurePeakQuiz(error: DatabaseError?) {
        databaseErrorPeak.value = error
    }

    override fun onSuccessEnergyQuiz(energy: String?) {
        energyQuizMutableLiveData.value = energy
    }

    override fun onFailureEnergyQuiz(error: DatabaseError?) {
        databaseErrorEnergy.value = error
    }

    override fun onSuccessTensionQuiz(tension: String?) {
        tensionQuizMutableLiveData.value = tension
    }

    override fun onFailureTensionQuiz(error: DatabaseError?) {
        databaseErrorTension.value = error
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

    private fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }


}