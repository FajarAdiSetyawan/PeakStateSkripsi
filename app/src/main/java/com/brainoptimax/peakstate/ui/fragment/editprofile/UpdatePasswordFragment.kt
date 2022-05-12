package com.brainoptimax.peakstate.ui.fragment.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentUpdatePasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UpdatePasswordFragment : Fragment() {

    // view binding
    private lateinit var binding: FragmentUpdatePasswordBinding

    // import FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // memanggil fragment update password menggunakan view binding
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // memanggil firebaseauth
        auth = FirebaseAuth.getInstance()

        // button save
        binding.btnSave.setOnClickListener {
            // mengambil text
            val oldPass = binding.tietOldPass.text.toString().trim()
            val newPass = binding.tietPassNew.text.toString().trim()
            val confPass = binding.tietPassComfirm.text.toString().trim()

            // pengecekan
            if (oldPass.isEmpty()) {
                binding.outlinedTextFieldOldPass.error = resources.getString(R.string.email_blank)
                binding.outlinedTextFieldOldPass.requestFocus()
                return@setOnClickListener
            } else if (newPass.isEmpty()) {
                binding.outlinedTextFieldPasswordNew.error =
                    resources.getString(R.string.password_blank)
                binding.outlinedTextFieldPasswordNew.requestFocus()
                return@setOnClickListener
            } else if (newPass.length < 8) {
                binding.outlinedTextFieldPasswordNew.error =
                    resources.getString(R.string.password_kurang)
                binding.outlinedTextFieldPasswordNew.requestFocus()
                return@setOnClickListener
            } else if (confPass != newPass) {
                binding.outlinedTextFieldPasswordConfirm.error =
                    resources.getString(R.string.password_dont_match)
                binding.outlinedTextFieldPasswordConfirm.requestFocus()
                return@setOnClickListener
            } else {
                // jika benar semua
                binding.outlinedTextFieldPasswordConfirm.error = null
                binding.outlinedTextFieldPasswordNew.error = null
                binding.outlinedTextFieldOldPass.error = null

                updatePassword(oldPass, newPass)
            }
        }
    }

    private fun updatePassword(oldPass: String, newPass: String){
        // progress bar terlihat
        binding.pbUpdatePassword.visibility = View.VISIBLE

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
                        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialogRounded)
                            .setTitle("UPDATE PASSWORD")
                            .setMessage(R.string.msg_update_password)
                            .setPositiveButton("Ok") { _, _ ->
                                user.let {
                                    // jika ya akan merubah ke password baru
                                    user.updatePassword(newPass).addOnCompleteListener {
                                        // pengecekan
                                        if (it.isSuccessful) {
                                            // jika berhasil ganti password baru
                                            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                                                fragmentTransaction?.replace(R.id.fragment_container, EditProfileFragment())?.commit()
                                            }
                                            binding.pbUpdatePassword.visibility = View.INVISIBLE
                                            Toast.makeText(
                                                activity!!,
                                                "Password Success Updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // jika gagal
                                            // progress bar tidak terlihat
                                            binding.pbUpdatePassword.visibility = View.INVISIBLE
                                            Toast.makeText(
                                                activity!!,
                                                "${it.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            .setNegativeButton(
                                "Cancel"
                            ) { _, _ ->
                                // progress bar tidak terlihat
                                binding.pbUpdatePassword.visibility = View.INVISIBLE
                                it.addOnCanceledListener {  }
                            }
                            .show()


                    }
                    // jika password lama salah
                    it.exception is FirebaseAuthInvalidCredentialsException -> {
                        // menampilkan pesan error di textfield
                        binding.outlinedTextFieldOldPass.error =
                            resources.getString(R.string.wrong_pass)
                        binding.outlinedTextFieldOldPass.requestFocus()
                        binding.pbUpdatePassword.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.pbUpdatePassword.visibility = View.INVISIBLE
                        Toast.makeText(
                            activity!!,
                            "${it.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}