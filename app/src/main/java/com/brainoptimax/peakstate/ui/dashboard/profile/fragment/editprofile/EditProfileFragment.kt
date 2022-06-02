package com.brainoptimax.peakstate.ui.dashboard.profile.fragment.editprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentEditProfileBinding
import com.brainoptimax.peakstate.ui.auth.AuthActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.Preferences
import com.brainoptimax.peakstate.utils.PreferencesKey
import com.brainoptimax.peakstate.viewmodel.profile.EditProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileFragment : Fragment() {

    private var fragmentEditProfileBinding: FragmentEditProfileBinding? = null
    private val binding get() = fragmentEditProfileBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var nav : NavController
    private lateinit var preferences: Preferences
    private val viewModel = EditProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEditProfileBinding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

        val user = auth.currentUser

        // Initialize Shared Preferences
        preferences = Preferences(activity!!)

        nav = Navigation.findNavController(requireView())

        binding.tietUsername.setText(preferences.getValues("username"))
        binding.tietEmailEdit.setText(preferences.getValues("email"))

        val fullname = preferences.getValues("fullname")

        if (fullname!!.isEmpty()){
            binding.tietFullname.setText("Full name")
        }else{
            binding.tietFullname.setText(fullname)
        }

        if (user != null) {
            binding.tietEmailEdit.setText(user.email)
            // cek email sudah verifikasi
            if (user.isEmailVerified) {
                binding.tvSendVerif.visibility = View.INVISIBLE
                binding.outlinedTextFieldEmail.helperText = resources.getString(R.string.verified)
                val colorInt = resources.getColor(R.color.md_green_400)
                val csl = ColorStateList.valueOf(colorInt)
                binding.outlinedTextFieldEmail.setHelperTextColor(csl)
            } else {
                // email belum diverifikasi
                val colorInt = resources.getColor(R.color.md_red_400)
                val csl = ColorStateList.valueOf(colorInt)
                binding.outlinedTextFieldEmail.helperText =
                    resources.getString(R.string.not_verified)
                binding.outlinedTextFieldEmail.setHelperTextColor(csl)
            }
        }

        binding.tvSendVerif.setOnClickListener {
            user?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, R.string.email_verif_send, Toast.LENGTH_SHORT).show()
                    auth.signOut() // fungsi dari firebase auth untuk logout
                    startActivity(Intent(context, AuthActivity::class.java)) // pindah ke login
                    Animatoo.animateSlideUp(requireContext())
                    Toast.makeText(context, "Success Logout", Toast.LENGTH_SHORT)
                        .show() //
                } else {
                    Toast.makeText(context, "${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val username = binding.tietUsername.text.toString().trim()
            val fullname = binding.tietFullname.text.toString().trim()

            when {
                username.isEmpty() -> {
                    binding.outlinedTextFieldUsername.error =
                        resources.getString(R.string.username_blank)
                    binding.outlinedTextFieldUsername.requestFocus()
                }
                fullname.isEmpty() -> {
                    binding.outlinedTextFieldFullName.error =
                        resources.getString(R.string.fullname_blank)
                    binding.outlinedTextFieldFullName.requestFocus()
                }
                else -> {
                    binding.outlinedTextFieldFullName.error = null
                    binding.outlinedTextFieldUsername.error = null

                    MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogRounded)
                        .setTitle("UPDATE PROFILE")
                        .setMessage("Are you sure want to Update Profile ?")
                        .setPositiveButton("Ok") { _, _ ->
                            viewModel.saveProfile(databaseReference, username, fullname, nav, view)

                            preferences.setValues(PreferencesKey.USERNAME, username)
                            preferences.setValues(PreferencesKey.FULLNAME, fullname)

                            viewModel.status.observe(activity!!) { status ->
                                status?.let {
                                    //Reset status value at first to prevent multitriggering
                                    //and to be available to trigger action again
                                    viewModel.status.value = null
                                    //Display Toast or snackbar
                                    Toast.makeText(
                                        activity,
                                        "Success Edit Profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { _, _ -> }
                        .show()
                }
            }
        }

        binding.tietEmailEdit.setOnClickListener {
            nav.navigate(R.id.action_editProfileFragment_to_updateEmailFragment)   //--> you can navigate to your main page
        }

        binding.tvChangePass.setOnClickListener {
            nav.navigate(R.id.action_editProfileFragment_to_updatePasswordFragment)   //--> you can navigate to your main page
        }
    }

}