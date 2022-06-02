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
import com.google.firebase.database.DatabaseReference

class UpdateEmailViewModel : ViewModel() {
    var status = MutableLiveData<Boolean?>()
    var error = MutableLiveData<Boolean?>()
    var statusMsg = MutableLiveData<Boolean?>()
    fun updateEmail(
        auth: FirebaseAuth,
        nav: NavController,
        databaseReference: DatabaseReference,
        email: String,
        password: String,
        view: View?,
        fragmentActivity: FragmentActivity
    ) {
        val user = auth.currentUser

        user?.let { updateEmail ->
            val userCredential = EmailAuthProvider.getCredential(updateEmail.email!!, password)
            updateEmail.reauthenticate(userCredential).addOnCompleteListener { it ->
                when {
                    it.isSuccessful -> {
                        MaterialAlertDialogBuilder(
                            fragmentActivity,
                            R.style.MaterialAlertDialogRounded
                        )
                            .setTitle("UPDATE EMAIL")
                            .setMessage(R.string.msg_update_email)
                            .setPositiveButton("Ok") { _, _ ->
                                user.let {
                                    user.updateEmail(email).addOnCompleteListener { it ->
                                        if (it.isSuccessful) {
                                            databaseReference.child("email").setValue(email)
                                                .addOnSuccessListener { }
                                                .addOnFailureListener {
                                                    val message: String = it.message.toString() // mengambil pesan error
                                                    Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                                                        .show()
                                                }

                                            nav.navigate(R.id.action_updateEmailFragment_to_editProfileFragment)
                                            status.value = true
                                            statusMsg.value = true

                                        } else {
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
                                status.value = true
                                it.addOnCanceledListener { }
                            }
                            .show()
                    }
                    it.exception is FirebaseAuthInvalidCredentialsException -> {
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