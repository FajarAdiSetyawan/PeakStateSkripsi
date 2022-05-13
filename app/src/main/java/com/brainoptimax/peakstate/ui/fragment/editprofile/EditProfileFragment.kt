package com.brainoptimax.peakstate.ui.fragment.editprofile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentEditProfileBinding
import com.brainoptimax.peakstate.ui.activity.MainActivity
import com.brainoptimax.peakstate.ui.activity.auth.AuthActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileFragment : Fragment() {

    private var binding: FragmentEditProfileBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Users").child(auth.currentUser!!.uid).child("fullname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val fullname = snapshot.value.toString()
                        binding?.tietFullname?.setText(fullname)
                    } else {
                        binding?.tietFullname?.setText(resources.getString(R.string.full_name))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding?.tietFullname?.setText(resources.getString(R.string.full_name))
                }
            })


        databaseReference.child("Users").child(auth.currentUser!!.uid).child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.value.toString()
                    binding?.tietUsername?.setText(username)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        if (user != null) {
            binding?.tietEmailEdit?.setText(user.email)

            if (user.isEmailVerified) {
                binding?.tvSendVerif?.visibility = View.INVISIBLE
                binding?.outlinedTextFieldEmail?.helperText = resources.getString(R.string.verified)
                val colorInt = resources.getColor(R.color.md_green_400)
                val csl = ColorStateList.valueOf(colorInt)
                binding?.outlinedTextFieldEmail?.setHelperTextColor(csl)
            } else {
                val colorInt = resources.getColor(R.color.md_red_400)
                val csl = ColorStateList.valueOf(colorInt)
                binding?.outlinedTextFieldEmail?.helperText =
                    resources.getString(R.string.not_verified)
                binding?.outlinedTextFieldEmail?.setHelperTextColor(csl)
            }
        }

        binding?.tvSendVerif?.setOnClickListener {
            user?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, R.string.email_verif_send, Toast.LENGTH_SHORT).show()
                    auth.signOut() // fungsi dari firebase auth untuk logout
                    startActivity(Intent(context, AuthActivity::class.java)) // pindah ke login
                    Animatoo.animateSlideUp(context!!)
                    Toast.makeText(context, "Success Logout", Toast.LENGTH_SHORT)
                        .show() //
                } else {
                    Toast.makeText(context, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.btnSave?.setOnClickListener {
            val username = binding?.tietUsername?.text.toString().trim()
            val fullname = binding?.tietFullname?.text.toString().trim()

            when {
                username.isEmpty() -> {
                    binding?.outlinedTextFieldUsername?.error =
                        resources.getString(R.string.username_blank)
                    binding?.outlinedTextFieldUsername?.requestFocus()
                }
                fullname.isEmpty() -> {
                    binding?.outlinedTextFieldFullName?.error =
                        resources.getString(R.string.fullname_blank)
                    binding?.outlinedTextFieldFullName?.requestFocus()
                }
                else -> {
                    binding?.outlinedTextFieldFullName?.error = null
                    binding?.outlinedTextFieldUsername?.error = null
                    save(username, fullname)
                }
            }
        }

        binding?.tietEmailEdit?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                fragmentTransaction?.replace(R.id.fragment_container, UpdateEmailFragment())?.commit()
            }
        }

        binding?.tvChangePass?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                fragmentTransaction?.replace(R.id.fragment_container, UpdatePasswordFragment())?.commit()
            }
        }
    }

    private fun save(username: String, fullname: String) {
        MaterialAlertDialogBuilder(context!!, R.style.MaterialAlertDialogRounded)
            .setTitle("UPDATE PROFILE")
            .setMessage("Are you sure want to Update Profile ?")
            .setPositiveButton("Ok") { _, _ ->
                databaseReference.child("Users").child(auth.currentUser!!.uid).child("username")
                    .setValue(username).addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Success Update Account!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
                        Animatoo.animateSwipeRight(context!!)
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Error Update!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                databaseReference.child("Users").child(auth.currentUser!!.uid).child("fullname")
                    .setValue(fullname).addOnSuccessListener { }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Error Update!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .setNegativeButton(
                "Cancel"
            ) { _, _ -> }
            .show()
    }

}