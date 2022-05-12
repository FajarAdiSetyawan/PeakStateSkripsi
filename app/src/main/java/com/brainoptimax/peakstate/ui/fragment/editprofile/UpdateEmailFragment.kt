package com.brainoptimax.peakstate.ui.fragment.editprofile

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentUpdateEmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateEmailFragment : Fragment() {

    private lateinit var binding: FragmentUpdateEmailBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.btnSave.setOnClickListener {
            val pass = binding.tietPassUpdateEmail.text.toString().trim()
            val email = binding.tietEmailUpdate.text.toString().trim()


            if (email.isEmpty()) {
                binding.outlinedTextFieldEmail.error = resources.getString(R.string.email_blank)
                binding.outlinedTextFieldEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.outlinedTextFieldEmail.error =
                    resources.getString(R.string.email_dont_match)
                binding.outlinedTextFieldEmail.requestFocus()
            } else if (pass.isEmpty()) {
                binding.outlinedTextFieldPassword.error =
                    resources.getString(R.string.password_blank)
                binding.outlinedTextFieldPassword.requestFocus()
            } else if (pass.length < 8) {
                binding.outlinedTextFieldPassword.error =
                    resources.getString(R.string.password_kurang)
                binding.outlinedTextFieldPassword.requestFocus()
            } else {
                binding.outlinedTextFieldEmail.error = null
                binding.outlinedTextFieldPassword.error = null

                updateEmail(email, pass)
            }
        }
    }


    private fun updateEmail(email: String, password: String) {
        binding.pbUpdateEmail.visibility = View.VISIBLE

        val user = auth.currentUser
        user?.let { updateEmail ->
            val userCredential = EmailAuthProvider.getCredential(updateEmail.email!!, password)
            updateEmail.reauthenticate(userCredential).addOnCompleteListener { it ->
                when {
                    it.isSuccessful -> {
                        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialogRounded)
                            .setTitle("UPDATE EMAIL")
                            .setMessage(R.string.msg_update_email)
                            .setPositiveButton("Ok") { _, _ ->
                                user.let {
                                    user.updateEmail(email).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            databaseReference.child("Users").child(auth.currentUser!!.uid)
                                                .child("email")
                                                .setValue(email).addOnSuccessListener { }
                                                .addOnFailureListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Error Update!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                                                fragmentTransaction?.replace(R.id.fragment_container, EditProfileFragment())?.commit()
                                            }
                                            binding.pbUpdateEmail.visibility = View.INVISIBLE
                                            Toast.makeText(activity, "Success Update Email", Toast.LENGTH_SHORT).show()
                                        } else {
                                            binding.pbUpdateEmail.visibility = View.INVISIBLE
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
                                binding.pbUpdateEmail.visibility = View.INVISIBLE
                                it.addOnCanceledListener {  }
                            }
                            .show()
                    }
                    it.exception is FirebaseAuthInvalidCredentialsException -> {
                        binding.outlinedTextFieldPassword.error =
                            resources.getString(R.string.wrong_pass)
                        binding.outlinedTextFieldPassword.requestFocus()
                        binding.pbUpdateEmail.visibility = View.INVISIBLE
                    }
                    else -> {
                        binding.pbUpdateEmail.visibility = View.INVISIBLE
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