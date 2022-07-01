package com.brainoptimax.peakmeup.viewmodel.reminder

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.model.Reminder
import com.brainoptimax.peakmeup.repository.ReminderRepository
import com.google.firebase.database.DatabaseError

class ReminderViewModel: ViewModel(),
        ReminderRepository.OnRealtimeDbAddReminder,
        ReminderRepository.OnRealtimeDbAddImgReminder,
        ReminderRepository.OnRealtimeDbUpdateReminder,
        ReminderRepository.OnRealtimeDbAllReminder,
        ReminderRepository.OnRealtimeDbDeleteReminder
{
    private lateinit var alertDialogLoading: AlertDialog

    private val reminderRepository: ReminderRepository = ReminderRepository(this, this, this, this, this)

    val addReminderMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddReminder = MutableLiveData<String?>()

    val imageReminderMutableLiveData = MutableLiveData<String?>()
    val databaseErrorImageReminder = MutableLiveData<String?>()

    val deleteReminderMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDeleteReminder = MutableLiveData<String?>()

    val updateReminderMutableLiveData = MutableLiveData<String?>()
    val databaseErrorUpdateReminder = MutableLiveData<String?>()

    val allReminderMutableLiveData = MutableLiveData<List<Reminder>>()
    val databaseErrorAllReminder = MutableLiveData<String?>()

    fun addReminder(
        uidUser: String,
        title: String,
        subtitle: String,
        desc: String,
        date: String,
        time: String,
    ){
        reminderRepository.addReminder(uidUser, title, subtitle, desc, date, time)
    }

    fun allReminder(uidUser: String) {
        reminderRepository.getAllReminder(uidUser)
    }

    fun updateReminder(
        uidUser: String,
        idReminder:String,
        title: String,
        subtitle: String,
        desc: String,
        date: String,
        time: String,
    ){
        reminderRepository.updateReminder(uidUser, idReminder, title, subtitle, desc, date, time)
    }

    fun setImageReminder(uidUser: String, idReminder: String, filePath: Uri){
        reminderRepository.setImgReminder(uidUser, idReminder, filePath)
    }

    fun deleteReminder(uidUser: String, idReminder: String, imgUrl: String){
        reminderRepository.deleteReminder(uidUser, idReminder, imgUrl)
    }

    override fun onSuccessAddReminder(status: String?) {
        addReminderMutableLiveData.value = status
    }

    override fun onFailureAddReminder(error: String?) {
        databaseErrorAddReminder.value = error
    }

    override fun onSuccessAddImgReminder(status: String?) {
        imageReminderMutableLiveData.value = status
    }

    override fun onFailureAddImgReminder(error: String?) {
        databaseErrorImageReminder.value = error
    }

    override fun onSuccessAllReminder(reminder: List<Reminder>) {
        allReminderMutableLiveData.value = reminder
    }

    override fun onFailureAllReminder(error: String?) {
        databaseErrorAllReminder.value = error!!
    }

    override fun onSuccessUpdateReminder(status: String?) {
        updateReminderMutableLiveData.value = status
    }

    override fun onFailureUpdateReminder(error: String?) {
        databaseErrorUpdateReminder.value = error
    }

    override fun onSuccessDeleteReminder(status: String?) {
        deleteReminderMutableLiveData.value = status
    }

    override fun onFailureDeleteReminder(error: String?) {
        databaseErrorDeleteReminder.value = error
    }

    fun openLoadingDialog(fragmentActivity: FragmentActivity) {
        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading, null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        alertDialogLoading = progress.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.show()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = fragmentActivity.resources.getString(R.string.msg_upload_pict)
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.playAnimation()
        lottie.setAnimation(R.raw.upload)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    fun openLoadingSaveDialog(fragmentActivity: FragmentActivity) {
        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading, null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        alertDialogLoading = progress.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.show()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = fragmentActivity.resources.getString(R.string.msg_send_save_reminder)
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.setAnimation(R.raw.send_email)
        lottie.playAnimation()
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }
}