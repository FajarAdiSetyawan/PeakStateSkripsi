package com.brainoptimax.peakstate.ui.activity.auth

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityRegisterBinding
import com.brainoptimax.peakstate.model.Users
import com.brainoptimax.peakstate.ui.activity.intro.IntroSliderActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import render.animations.*

class RegisterActivity : AppCompatActivity() {
    private var activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = activityRegisterBinding!!

    private lateinit var auth: FirebaseAuth
    private val networkMonitor = NetworkMonitorUtil(this)

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123

    private val animation = Render(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        animation.setAnimation(Bounce.InDown(binding.ivIconSignin))
        animation.start()

        animation.setAnimation(Bounce.InUp(binding.tvLoginSignup))
        animation.start()

        animation.setAnimation(Fade.In(binding.llSignin))
        animation.start()

        checkConnection()

        // inisialiasi firebase auth
        auth = FirebaseAuth.getInstance()


        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        binding.btnRegister.setOnClickListener {

            // mengambil data dari textview
            val username = binding.tietUsername.text.toString().trim()
            val email = binding.tietEmailSignup.text.toString().trim()
            val password = binding.tietPassSignup.text.toString().trim()

            if (username.isEmpty()) {
                binding.outlinedTextFieldUsername.error =
                    resources.getString(R.string.username_blank)
                binding.outlinedTextFieldUsername.requestFocus()
            } else if (email.isEmpty()) {
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
                binding.outlinedTextFieldEmail.error = null
                binding.outlinedTextFieldPass.error = null
                binding.outlinedTextFieldUsername.error = null
                register(username, email, password)
            }

        }

        binding.tvLoginSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        binding.btnGoogleRegis.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Req_Code)
        }

    }

    private fun register(username: String, email: String, password: String) {
        // tampilkan dialog loading register
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        val alertDialogLoading = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()

        // buat akun baru, kirim data ke firebase(Authentication)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { it ->
                // jika sukses
                if (it.isSuccessful) {
                    // buat data baru di firebase(Realtime database)
                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(auth.currentUser!!.uid) // simpan ke firebase realtime database -> root -> users
                    val users = Users(
                        auth.currentUser!!.uid,
                        email,
                        username
                    )  // -> dengan data dari model Users
                    // simpan sebagai user di firebase realtime database = root->users->(userid, email, username)
                    ref.setValue(users).addOnCompleteListener {
                        // jika berhasil disimpan
                        if (it.isSuccessful) {
                            alertDialogLoading.dismiss() // dialog loading hilang
                            // pindah ke activity
                            startActivity(Intent(this, IntroSliderActivity::class.java))
                            finish()
                            Animatoo.animateZoom(this) //fire the zoom animation
                            Toast.makeText(
                                this,
                                "" + resources.getString(R.string.welcome) + "\n" + username,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // tampilkan dialog error
                            alertDialogLoading.dismiss() // dialog loading hilang
                            val message: String =
                                it.exception!!.message.toString() // mengambil pesan error
                            val li = LayoutInflater.from(this)
                            // panggil dialog eror
                            val promptsView: View =
                                li.inflate(R.layout.dialog_error_secondary, null)
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setView(promptsView)
                            alertDialogBuilder.setCancelable(true)
                            val dialogError = alertDialogBuilder.create()
                            val back = ColorDrawable(Color.TRANSPARENT)
                            val inset = InsetDrawable(back, 150)
                            val messageView =
                                promptsView.findViewById<View>(R.id.tv_msg_dialog_error_auth) as TextView
                            messageView.text = message
                            promptsView.findViewById<View>(R.id.iv_dismiss_error_auth)
                                .setOnClickListener { dialogError.dismiss() }
                            dialogError.window!!.setBackgroundDrawable(inset)
                            dialogError.show()
                        }

                    }
                } else {
                    // jika gagal membuat akun baru
                    alertDialogLoading.dismiss() // dialog loading hilang
                    // mengambil pesan error
                    val message: String = it.exception!!.message.toString()
                    val li = LayoutInflater.from(this)
                    val promptsView: View = li.inflate(R.layout.dialog_error_secondary, null)
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setView(promptsView)
                    alertDialogBuilder.setCancelable(true)
                    val dialogError = alertDialogBuilder.create()
                    val back = ColorDrawable(Color.TRANSPARENT)
                    val inset = InsetDrawable(back, 150)
                    val messageView =
                        promptsView.findViewById<View>(R.id.tv_msg_dialog_error_auth) as TextView
                    messageView.text = message
                    promptsView.findViewById<View>(R.id.iv_dismiss_error_auth)
                        .setOnClickListener { dialogError.dismiss() }
                    dialogError.window!!.setBackgroundDrawable(inset)
                    dialogError.show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                loginWithGoogle(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun loginWithGoogle(account: GoogleSignInAccount) {
        // tampilkan dialog loading register
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        val alertDialogLoading = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        // panggil textview judul error dari layout dialog
        val titleView = promptsView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = (resources.getString(R.string.loading_login))
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val isNew = task.result.additionalUserInfo!!.isNewUser
                if (!isNew) {
                    alertDialogLoading.dismiss() // dialog loading hilang
                    // pindah ke activity
                    startActivity(Intent(this, IntroSliderActivity::class.java))
                    finish()
                    Animatoo.animateZoom(this) //fire the zoom animation
                    Toast.makeText(
                        this,
                        "" + resources.getString(R.string.welcome) + "\n" + account.displayName.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // buat data baru di firebase(Realtime database)
                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(auth.currentUser!!.uid) // simpan ke firebase realtime database -> root -> users
                    val users = Users(
                        auth.currentUser!!.uid,
                        account.email.toString(),
                        account.displayName.toString(),
                        account.familyName.toString(),
                        account.photoUrl.toString()
                    )  // -> dengan data dari model Users
                    // simpan sebagai user di firebase realtime database = root->users->(userid, email, username)
                    ref.setValue(users).addOnCompleteListener {
                        // jika berhasil disimpan
                        if (it.isSuccessful) {
                            alertDialogLoading.dismiss() // dialog loading hilang
                            // pindah ke activity
                            startActivity(Intent(this, IntroSliderActivity::class.java))
                            finish()
                            Animatoo.animateZoom(this) //fire the zoom animation
                            Toast.makeText(
                                this,
                                "" + resources.getString(R.string.welcome) + "\n" + account.displayName.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // tampilkan dialog error
                            alertDialogLoading.dismiss() // dialog loading hilang
                            val message: String =
                                it.exception!!.message.toString() // mengambil pesan error
                            val li = LayoutInflater.from(this)
                            // panggil dialog eror
                            val promptsView: View =
                                li.inflate(R.layout.dialog_error_secondary, null)
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setView(promptsView)
                            alertDialogBuilder.setCancelable(true)
                            val dialogError = alertDialogBuilder.create()
                            val back = ColorDrawable(Color.TRANSPARENT)
                            val inset = InsetDrawable(back, 150)
                            val messageView =
                                promptsView.findViewById<View>(R.id.tv_msg_dialog_error_auth) as TextView
                            messageView.text = message
                            promptsView.findViewById<View>(R.id.iv_dismiss_error_auth)
                                .setOnClickListener { dialogError.dismiss() }
                            dialogError.window!!.setBackgroundDrawable(inset)
                            dialogError.show()
                        }
                    }

                }
            } else {
                // jika gagal membuat akun baru
                alertDialogLoading.dismiss() // dialog loading hilang
                // mengambil pesan error
                val message: String = task.exception!!.message.toString()
                val li = LayoutInflater.from(this)
                val promptsView: View = li.inflate(R.layout.dialog_error_secondary, null)
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setView(promptsView)
                alertDialogBuilder.setCancelable(true)
                val dialogError = alertDialogBuilder.create()
                val back = ColorDrawable(Color.TRANSPARENT)
                val inset = InsetDrawable(back, 150)
                val messageView =
                    promptsView.findViewById<View>(R.id.tv_msg_dialog_error_auth) as TextView
                messageView.text = message
                promptsView.findViewById<View>(R.id.iv_dismiss_error_auth)
                    .setOnClickListener { dialogError.dismiss() }
                dialogError.window!!.setBackgroundDrawable(inset)
                dialogError.show()
            }
        }
    }

    // fungsi untuk mengecek internet secara berulang
    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    // fungsi untuk menghentikan pengecekan internet
    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityRegisterBinding = null
    }

    // Check Internet Connection
    private fun checkConnection() {
        // buat dialog error internet
        val li = LayoutInflater.from(this)
        // panggil layout dialog error
        val promptsView: View = li.inflate(R.layout.dialog_error, null)
        val alertDialogBuilder = AlertDialog.Builder(
            this
        )
        alertDialogBuilder.setView(promptsView)
        // dialog error tidak dapat di tutup selain menekan tombol close
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20)

        // panggil textview judul error dari layout dialog
        val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_error) as TextView
        titleView.text = (resources.getString(R.string.offline))

        // panggil textview desc error dari layout dialog
        val descView = promptsView.findViewById<View>(R.id.tv_desc_dialog_error) as TextView
        descView.text = (resources.getString(R.string.msgoffline))

        // panggil animasi lottie error dari layout dialog
        val lottieView =
            promptsView.findViewById<View>(R.id.lottie_dialog_error) as LottieAnimationView
        lottieView.playAnimation()
        lottieView.setAnimation(R.raw.offline)

        // panggil button error dari layout dialog
        val btnView = promptsView.findViewById<View>(R.id.btn_error_dialog) as Button
        btnView.text = (resources.getString(R.string.connect))
        btnView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            this.let { it1 -> Animatoo.animateSlideUp(it1) }
        }
        // panggil gambar close error dari layout dialog
        val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_error_dialog) as ImageView
        ivDismiss.setOnClickListener {
            networkMonitor.result = { isAvailable, type ->
                runOnUiThread {
                    // cek ada status wifi/data seluler
                    when (isAvailable) {
                        true -> {
                            when (type) {
                                ConnectionType.Wifi -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                    // dialog error ditutup
                                    alertDialog.dismiss()
                                }
                                ConnectionType.Cellular -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                    alertDialog.dismiss()
                                }
                                else -> {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                        false -> {
                            Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                            // dialog error ditampilkan
                            alertDialog.show()
                        }
                    }
                }
            }
        }
        alertDialog.window!!.setBackgroundDrawable(inset)
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                alertDialog.dismiss()
                            }
                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                alertDialog.dismiss()
                            }
                            else -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        alertDialog.show()
                    }
                }
            }
        }
    }
}