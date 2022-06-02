package com.brainoptimax.peakstate.viewmodel.profile

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.brainoptimax.peakstate.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UpdatePasswordViewModel: ViewModel() {
    var status = MutableLiveData<Boolean?>()
    var statusMsg = MutableLiveData<Boolean?>()
    var error = MutableLiveData<Boolean?>()

    fun updatePassword(
        auth: FirebaseAuth,
        nav: NavController,
        oldPass: String,
        newPass: String,
        view: View?,
        fragmentActivity: FragmentActivity
    ){
        // mengambil data user yg login
        val user = auth.currentUser
        user?.let { updatePassword ->
            // mengambil data email, dan password dari firebase
            val userCredential = EmailAuthProvider.getCredential(updatePassword.email!!, oldPass)
            // fungsi update password
            updatePassword.reauthenticate(userCredential).addOnCompleteListener { it ->
                when {
                    // jika sukses
                    it.isSuccessful -> {
                        // buat dialog konfirmasi
                        MaterialAlertDialogBuilder(fragmentActivity, R.style.MaterialAlertDialogRounded)
                            .setTitle("UPDATE PASSWORD")
                            .setMessage(R.string.msg_update_password)
                            .setPositiveButton("Ok") { _, _ ->
                                user.let {
                                    // jika ya akan merubah ke password baru
                                    user.updatePassword(newPass).addOnCompleteListener {
                                        // pengecekan
                                        if (it.isSuccessful) {
                                            // jika berhasil ganti password baru
                                            nav.navigate(R.id.action_updatePasswordFragment_to_editProfileFragment)
                                            status.value = true
                                            statusMsg.value = true
                                        } else {
                                            // jika gagal
                                            // progress bar tidak terlihat
                                            status.value = true
                                            val message: String = it.exception!!.message.toString() // mengambil pesan error
                                            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                                                .show()
                                        }
                                    }
                                }
                            }
                            .setNegativeButton(
                                "Cancel"
                            ) { _, _ ->
                                // progress bar tidak terlihat
                                status.value = true
                                it.addOnCanceledListener {  }
                            }
                            .show()


                    }
                    // jika password lama salah
                    it.exception is FirebaseAuthInvalidCredentialsException -> {
                        // menampilkan pesan error di textfield
                        error.value = true
                        status.value = true
                    }
                    else -> {
                        status.value = true
                        val message: String = it.exception!!.message.toString() // mengambil pesan error
                        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}