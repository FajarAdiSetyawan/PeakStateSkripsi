package com.brainoptimax.peakmeup.viewmodel.bottomnav

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakmeup.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class ProfileViewModel: ViewModel() {
    private lateinit var alertDialogLoading: AlertDialog

    @SuppressLint("NullSafeMutableLiveData")
    internal fun setSignUpPhotoScreen(
        mDatabase: DatabaseReference,
        mStorageRef: StorageReference,
        uid: String,
        filePath: Uri,
        view: View?
    ): LiveData<String> {
        val signUpPhotoScreenResult = MutableLiveData<String>()
        val ref = mStorageRef.child("users/$uid/profile.jpg")

        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    // Insert url data to user
                    closeLoadingDialog()
                    mDatabase.child(uid).child("photoUrl").setValue(it.toString())
                    signUpPhotoScreenResult.postValue(it.toString())

                    Snackbar.make(view!!, "Image Uploaded", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            .addOnFailureListener {
                closeLoadingDialog()
                signUpPhotoScreenResult.postValue(null)

                Snackbar.make(view!!, it.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }

        return signUpPhotoScreenResult
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
        val lottie = dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.playAnimation()
        lottie.setAnimation(R.raw.upload)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    private fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }
}