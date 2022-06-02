package com.brainoptimax.peakstate.ui.dashboard.profile.fragment.editprofile

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentUpdateEmailBinding
import com.brainoptimax.peakstate.utils.Preferences
import com.brainoptimax.peakstate.viewmodel.profile.UpdateEmailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateEmailFragment : Fragment() {

    private lateinit var binding: FragmentUpdateEmailBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nav : NavController
    private lateinit var preferences: Preferences

    private val viewModel = UpdateEmailViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Initialize Shared Preferences
        preferences = Preferences(activity!!)

        nav = Navigation.findNavController(requireView())

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

        binding.btnSave.setOnClickListener {
            val pass = binding.tietPassUpdateEmail.text.toString().trim()
            val email = binding.tietEmailUpdate.text.toString().trim()


            when {
                email.isEmpty() -> {
                    binding.outlinedTextFieldEmail.error = resources.getString(R.string.email_blank)
                    binding.outlinedTextFieldEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.outlinedTextFieldEmail.error =
                        resources.getString(R.string.email_dont_match)
                    binding.outlinedTextFieldEmail.requestFocus()
                }
                pass.isEmpty() -> {
                    binding.outlinedTextFieldPassword.error =
                        resources.getString(R.string.password_blank)
                    binding.outlinedTextFieldPassword.requestFocus()
                }
                pass.length < 8 -> {
                    binding.outlinedTextFieldPassword.error =
                        resources.getString(R.string.password_kurang)
                    binding.outlinedTextFieldPassword.requestFocus()
                }
                else -> {
                    binding.outlinedTextFieldEmail.error = null
                    binding.outlinedTextFieldPassword.error = null

                    updateEmail(email, pass)
                }
            }
        }

        binding.backMain.setOnClickListener {
            nav.navigate(R.id.action_updateEmailFragment_to_editProfileFragment)
        }
    }


    private fun updateEmail(email: String, password: String) {
        binding.pbUpdateEmail.visibility = View.VISIBLE

        viewModel.updateEmail(auth, nav, databaseReference, email, password, view, requireActivity())

        viewModel.status.observe(this) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.status.value = null
                binding.pbUpdateEmail.visibility = View.INVISIBLE
            }
        }

        viewModel.error.observe(this) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.error.value = null
                binding.outlinedTextFieldPassword.error =
                    resources.getString(R.string.wrong_pass)
                binding.outlinedTextFieldPassword.requestFocus()
            }
        }

        viewModel.statusMsg.observe(this) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.statusMsg.value = null
                Toast.makeText(activity, "Success Update Email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}