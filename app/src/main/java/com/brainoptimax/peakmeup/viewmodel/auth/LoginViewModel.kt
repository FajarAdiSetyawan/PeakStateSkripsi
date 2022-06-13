package com.brainoptimax.peakmeup.viewmodel.auth

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.brainoptimax.peakmeup.model.Users
import com.brainoptimax.peakmeup.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginViewModel : ViewModel() {

    private lateinit var alertDialogLoading: AlertDialog

    fun loginWithEmail(
        mDatabaseReference: DatabaseReference,
        email: String,
        password: String,
        auth: FirebaseAuth,
        nav: NavController,
        view: View?
    ) : LiveData<Users> {
        val signInResult = MutableLiveData<Users>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                closeLoadingDialog()
                if (task.isSuccessful) {
                    mDatabaseReference.child(auth.uid.toString())
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.d("Error Get Data: ", p0.message)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val user = p0.getValue(Users::class.java)
                                signInResult.postValue(user!!)
                            }
                        })

                } else {
                    // tampilkan dialog error
                    val message: String =
                        task.exception!!.message.toString() // mengambil pesan error
                    Snackbar.make(view!!,message, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        return signInResult
    }

    fun saveNewAccountGoogle(
        uid : String?, email : String?, displayName: String?, familyName: String?, photoUrl: String?, nav: NavController, view: View?
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid!!) // simpan ke firebase realtime database -> root -> users
        val users = Users(
            uid,
            email,
            displayName,
            familyName,
            photoUrl
        )  // -> dengan data dari model Users
        // simpan sebagai user di firebase realtime database = root->users->(userid, email, username)
        ref.setValue(users).addOnCompleteListener {
            // jika berhasil disimpan
            if (it.isSuccessful) {
                nav.navigate(R.id.action_loginFragment_to_introSliderActivity)
            } else {
                // tampilkan dialog error
                val message: String =
                    it.exception!!.message.toString() // mengambil pesan error
                Snackbar.make(view!!,message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }


    fun openLoadingDialog(fragmentActivity: FragmentActivity) {

        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading,null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        alertDialogLoading = progress.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.show()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = fragmentActivity.resources.getString(R.string.loading_login)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }
}