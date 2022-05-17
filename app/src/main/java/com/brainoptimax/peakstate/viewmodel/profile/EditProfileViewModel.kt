package com.brainoptimax.peakstate.viewmodel.profile

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.model.Users
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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