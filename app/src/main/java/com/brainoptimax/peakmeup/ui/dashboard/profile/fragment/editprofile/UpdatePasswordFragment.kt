package com.brainoptimax.peakmeup.ui.dashboard.profile.fragment.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.profile.UpdatePasswordViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentUpdatePasswordBinding
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordFragment : Fragment() {

    // view binding
    private lateinit var binding: FragmentUpdatePasswordBinding

    // import FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var nav : NavController
    private lateinit var preferences: Preferences

    private val viewModel = UpdatePasswordViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // memanggil fragment update password menggunakan view binding
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // memanggil firebaseauth
        auth = FirebaseAuth.getInstance()

        // Initialize Shared Preferences
        preferences = Preferences(requireActivity())

        nav = Navigation.findNavController(requireView())

        // button save
        binding.btnSave.setOnClickListener {
            // mengambil text
            val oldPass = binding.tietOldPass.text.toString().trim()
            val newPass = binding.tietPassNew.text.toString().trim()
            val confPass = binding.tietPassComfirm.text.toString().trim()

            // pengecekan
            when {
                oldPass.isEmpty() -> {
                    binding.outlinedTextFieldOldPass.error = resources.getString(R.string.email_blank)
                    binding.outlinedTextFieldOldPass.requestFocus()
                    return@setOnClickListener
                }
                newPass.isEmpty() -> {
                    binding.outlinedTextFieldPasswordNew.error =
                        resources.getString(R.string.password_blank)
                    binding.outlinedTextFieldPasswordNew.requestFocus()
                    return@setOnClickListener
                }
                newPass.length < 8 -> {
                    binding.outlinedTextFieldPasswordNew.error =
                        resources.getString(R.string.password_kurang)
                    binding.outlinedTextFieldPasswordNew.requestFocus()
                    return@setOnClickListener
                }
                confPass != newPass -> {
                    binding.outlinedTextFieldPasswordConfirm.error =
                        resources.getString(R.string.password_dont_match)
                    binding.outlinedTextFieldPasswordConfirm.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    // jika benar semua
                    binding.outlinedTextFieldPasswordConfirm.error = null
                    binding.outlinedTextFieldPasswordNew.error = null
                    binding.outlinedTextFieldOldPass.error = null

                    updatePassword(oldPass, newPass)
                }
            }
        }

        binding.backMain.setOnClickListener {
            nav.navigate(R.id.action_updatePasswordFragment_to_editProfileFragment)
        }
    }

    private fun updatePassword(oldPass: String, newPass: String){
        // progress bar terlihat
        binding.pbUpdatePassword.visibility = View.VISIBLE

        viewModel.updatePassword(auth, nav, oldPass, newPass, view, requireActivity())
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.status.value = null
                binding.pbUpdatePassword.visibility = View.INVISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.error.value = null
                binding.outlinedTextFieldOldPass.error =
                    resources.getString(R.string.wrong_pass)
                binding.outlinedTextFieldOldPass.requestFocus()
            }
        }

        viewModel.statusMsg.observe(viewLifecycleOwner) { status ->
            status?.let {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.statusMsg.value = null
                Toast.makeText(activity, "Success Update Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}