package com.brainoptimax.peakmeup.viewmodel.profile

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.brainoptimax.peakmeup.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference

class EditProfileViewModel: ViewModel() {
    var status = MutableLiveData<Boolean?>()

    fun saveProfile(
        databaseReference: DatabaseReference,
        username: String?,
        fullname: String?,
        nav: NavController,
        view: View?
    ) {
        databaseReference.child("username").setValue(username)
        databaseReference.child("fullname").setValue(fullname).addOnSuccessListener {
            nav.navigate(R.id.action_editProfileFragment_to_mainActivity)
            status.value = true
        }.addOnFailureListener {
            val message: String =
                it.message.toString() // mengambil pesan error
            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                .show()
        }
    }


}