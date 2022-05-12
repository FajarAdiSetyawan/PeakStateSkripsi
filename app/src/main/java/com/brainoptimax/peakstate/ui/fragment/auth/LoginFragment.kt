package com.brainoptimax.peakstate.ui.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentLoginBinding
import com.brainoptimax.peakstate.viewmodel.auth.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private val binding get() = fragmentLoginBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel = LoginViewModel()
    private lateinit var nav : NavController

    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        nav = Navigation.findNavController(requireView())

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)


        binding.btnGoogleLogin.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tietEmailSignin.text.toString().trim()
            val password = binding.tietPassSignin.text.toString().trim()

            if (email.isEmpty()) {
                binding.outlinedTextFieldEmail.error =
                    resources.getString(R.string.email_blank)
                binding.outlinedTextFieldEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.outlinedTextFieldEmail.error =
                    resources.getString(R.string.email_dont_match)
                binding.outlinedTextFieldEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.outlinedTextFieldPass.error =
                    resources.getString(R.string.password_blank)
                binding.outlinedTextFieldPass.requestFocus()
            } else if (password.length < 8) {
                binding.outlinedTextFieldPass.error =
                    resources.getString(R.string.password_kurang)
                binding.outlinedTextFieldPass.requestFocus()
            } else {
                binding.outlinedTextFieldPass.error = null
                binding.outlinedTextFieldPass.error = null

                viewModel.openLoadingDialog(requireActivity())
                viewModel.loginWithEmail(email, password, auth, nav, view)
            }
        }

        binding.tvForgotpass.setOnClickListener {
            nav.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.tvRegisterNow.setOnClickListener {
            nav.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } catch (e: ApiException) {
            Snackbar.make(requireView(), e.message.toString(), Snackbar.LENGTH_LONG)
                .show()
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) { // with this func. you can easily sign in with google
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModel.openLoadingDialog(requireActivity())  // show dialog
        // function sign in with google
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // check user baru
                    val isNew = task.result.additionalUserInfo!!.isNewUser
                    if (!isNew) {
                        // jika tidak user baru
                        viewModel.closeLoadingDialog()  // tutup dialog
                        nav.navigate(R.id.action_loginFragment_to_introSliderActivity)  // pindah ke intro
                        Toast.makeText(activity, "" + resources.getString(R.string.welcome) + "\n" + account.displayName.toString(), Toast.LENGTH_LONG).show()
                        //
                    } else {
                        // jika user baru
                        viewModel.saveNewAccountGoogle(auth.currentUser?.uid, account.email.toString(), account.displayName.toString(), account.familyName.toString(), account.photoUrl.toString(), nav, view)
                        Toast.makeText(activity, "" + resources.getString(R.string.welcome) + "\n" + account.displayName.toString(), Toast.LENGTH_LONG).show()
                        nav.navigate(R.id.action_loginFragment_to_introSliderActivity)   //--> you can navigate to your main page
                        viewModel.closeLoadingDialog()
                    }
                } else {
                    viewModel.closeLoadingDialog()
                    Snackbar.make(requireView(), task.exception!!.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }

}