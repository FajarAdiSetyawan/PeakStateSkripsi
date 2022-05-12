package com.brainoptimax.peakstate.viewmodel.auth

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.model.Users
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.net.ProtocolFamily

class LoginViewModel : ViewModel() {

    private lateinit var alertDialogLoading: AlertDialog

    fun loginWithEmail(
        email: String,
        password: String,
        auth: FirebaseAuth,
        nav: NavController,
        view: View?
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                closeLoadingDialog()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    nav.navigate(R.id.action_loginFragment_to_introSliderActivity)
                } else {
                    // tampilkan dialog error

                    val message: String =
                        task.exception!!.message.toString() // mengambil pesan error
                    Snackbar.make(view!!,message, Snackbar.LENGTH_LONG)
                        .show()
                }
            }

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
                Snackbar.make(view!!,view.resources.getString(R.string.welcome) + "\n" + displayName, Snackbar.LENGTH_LONG)
                    .show()
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