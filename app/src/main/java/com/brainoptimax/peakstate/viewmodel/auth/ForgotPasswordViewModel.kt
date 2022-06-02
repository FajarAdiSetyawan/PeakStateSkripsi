package com.brainoptimax.peakstate.viewmodel.auth

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private lateinit var alertDialogLoading: AlertDialog

    fun sendResetEmail(
        email: String,
        auth: FirebaseAuth, view: View?
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(view!!, R.string.check_email_reset_pass, Snackbar.LENGTH_LONG)
                        .show()
                    closeLoadingDialog()

                } else {
                    closeLoadingDialog()
                    Snackbar.make(view!!, task.exception!!.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
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
        titleView.text = fragmentActivity.resources.getString(R.string.msg_send_forgot_pass)
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.setAnimation(R.raw.send_email)
        lottie.playAnimation()
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    private fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }
}