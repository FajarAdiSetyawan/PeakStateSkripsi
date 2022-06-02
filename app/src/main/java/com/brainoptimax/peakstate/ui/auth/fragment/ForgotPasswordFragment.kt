package com.brainoptimax.peakstate.ui.auth.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentForgotPasswordBinding
import com.brainoptimax.peakstate.viewmodel.auth.ForgotPasswordViewModel
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordFragment : Fragment() {

    private var fragmentForgotPasswordBinding: FragmentForgotPasswordBinding? = null
    private val binding get() = fragmentForgotPasswordBinding!!

    private lateinit var nav : NavController
    private lateinit var auth: FirebaseAuth
    private val viewModel = ForgotPasswordViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentForgotPasswordBinding =
            FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        nav = Navigation.findNavController(requireView())

        binding.tvLoginReset.setOnClickListener {
            nav.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }

        binding.btnReset.setOnClickListener {
            val email = binding.tietEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.outlinedTextFieldEmail.error =
                    resources.getString(R.string.email_blank)
                binding.outlinedTextFieldEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.outlinedTextFieldEmail.error =
                    resources.getString(R.string.email_dont_match)
                binding.outlinedTextFieldEmail.requestFocus()
            }else{
                binding.outlinedTextFieldEmail.error = null
                viewModel.openLoadingDialog(requireActivity())
                viewModel.sendResetEmail(email, auth, view)

            }
        }
    }

}