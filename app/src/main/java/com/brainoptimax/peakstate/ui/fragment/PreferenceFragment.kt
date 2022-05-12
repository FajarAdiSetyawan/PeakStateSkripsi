package com.brainoptimax.peakstate.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.ui.activity.auth.AuthActivity
import com.brainoptimax.peakstate.ui.activity.auth.LoginActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakstate.ui.activity.quiz.ResultEnergyQuizActivity
import com.brainoptimax.peakstate.ui.activity.quiz.ResultPSRQuizActivity
import com.brainoptimax.peakstate.ui.fragment.bottomnav.HomeFragment
import com.brainoptimax.peakstate.ui.fragment.editprofile.EditProfileFragment
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var prefTheme: ListPreference
    private lateinit var prefLang: ListPreference
    private lateinit var prefProfile: Preference
    private lateinit var prefEnergy: Preference
    private lateinit var prefPeak: Preference
    private lateinit var prefShare: Preference
    private lateinit var prefFeedback: Preference
    private lateinit var prefLogout: Preference


    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // memanggil layout pref_main
        addPreferencesFromResource(R.xml.pref_main)
        initComponents()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)
    }

    private fun initComponents() {

        // panggil setting tema dari key di pref_main
        prefTheme = findPreference<ListPreference>(getString(R.string.pref_key_theme))
                as ListPreference
        // buat fungsi click jika ditekan
        prefTheme.onPreferenceChangeListener = onThemePreferenceChange()


        prefProfile = findPreference<Preference>(getString(R.string.pref_key_accout))
                as Preference
        prefProfile.onPreferenceClickListener = onPreferenceProfile()

        prefEnergy = findPreference<Preference>(getString(R.string.pref_key_energy))
                as Preference
        prefEnergy.onPreferenceClickListener = onPreferenceEnergy()

        prefPeak = findPreference<Preference>(getString(R.string.pref_key_peak))
                as Preference
        prefPeak.onPreferenceClickListener = onPreferencePeak()

        prefFeedback = findPreference<Preference>(getString(R.string.pref_key_feedback))
                as Preference
        prefFeedback.onPreferenceClickListener = onPreferenceFeedback()

        prefShare = findPreference<Preference>(getString(R.string.pref_key_share))
                as Preference
        prefShare.onPreferenceClickListener = onPreferenceShare()

        prefLogout = findPreference<Preference>(getString(R.string.pref_key_logout))
                as Preference
        prefLogout.onPreferenceClickListener = onPreferenceLogout()



    }


    private fun onThemePreferenceChange(): Preference.OnPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            // pengecekan tema
            when {
                newValue.equals(getString(R.string.pref_theme_entry_auto)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                newValue.equals(getString(R.string.pref_theme_entry_dark)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                newValue.equals(getString(R.string.pref_theme_entry_light)) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            true
        }


    private fun updateTheme(mode: Int): Boolean {
        // pindah ke fragment home
        activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
            fragmentTransaction?.replace(R.id.fragment_container, HomeFragment())?.commit()
        }

        // ubah icon ke icon home
        val bottomNavigationView: BottomNavigationView =
            activity?.findViewById(R.id.navigation) as BottomNavigationView
        bottomNavigationView.menu.findItem(R.id.nav_home).isChecked = true

        // ubah tema diambil dari user
        AppCompatDelegate.setDefaultNightMode(mode)
        // buat ulang activity
        requireActivity().recreate()
        return true
    }

    private fun onLangPreferenceChange(): Preference.OnPreferenceChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->

            true
        }



    private fun onPreferenceProfile(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                fragmentTransaction?.replace(R.id.fragment_container, EditProfileFragment())?.commit()
            }

            // ubah icon ke icon home
            val bottomNavigationView: BottomNavigationView =
                activity?.findViewById(R.id.navigation) as BottomNavigationView
            bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true
            true
        }

    private fun onPreferenceEnergy(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {

            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Tension")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    @SuppressLint("CutPasteId")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            startActivity(Intent(activity!!, ResultEnergyQuizActivity::class.java)) // pindah ke login
                            Animatoo.animateSwipeLeft(activity!!)
                        } else {
                            // buat dialog error internet
                            val li = LayoutInflater.from(activity!!)
                            // panggil layout dialog error
                            val promptsView: View = li.inflate(R.layout.dialog_quiz, null)
                            val alertDialogBuilder = AlertDialog.Builder(
                                activity!!
                            )
                            alertDialogBuilder.setView(promptsView)
                            // dialog error tidak dapat di tutup selain menekan tombol close
                            alertDialogBuilder.setCancelable(true)
                            val alertDialog = alertDialogBuilder.create()
                            val back = ColorDrawable(Color.TRANSPARENT)
                            val inset = InsetDrawable(back, 20)
                            alertDialog.window!!.setBackgroundDrawable(inset)

                            val cardView = promptsView.findViewById<View>(R.id.card_dialog_quiz) as MaterialCardView
                            cardView.strokeColor = ContextCompat.getColor(activity!!, R.color.md_orange_400)
                            cardView.strokeWidth = 5

                            // panggil textview judul error dari layout dialog
                            val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_quiz) as TextView
                            titleView.text = (resources.getString(R.string.take_quiz_energy))

                            // panggil gambar close error dari layout dialog
                            val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_dialog_quiz) as ImageView
                            ivDismiss.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            val noTake = promptsView.findViewById<View>(R.id.tv_no_take_quiz) as TextView
                            noTake.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            val takeQuiz = promptsView.findViewById<View>(R.id.tv_take_quiz) as TextView
                            takeQuiz.setOnClickListener {
                                startActivity(Intent(activity!!, IntroEnergyTensionActivity::class.java)) // pindah ke login
                                Animatoo.animateSwipeLeft(activity!!)
                            }
                            alertDialog.show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            true
        }

    private fun onPreferencePeak(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("PSR")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    @SuppressLint("CutPasteId")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            startActivity(Intent(activity!!, ResultPSRQuizActivity::class.java)) // pindah ke login
                            Animatoo.animateSwipeLeft(activity!!)
                        } else {
                            // buat dialog error internet
                            val li = LayoutInflater.from(activity!!)
                            // panggil layout dialog error
                            val promptsView: View = li.inflate(R.layout.dialog_quiz, null)
                            val alertDialogBuilder = AlertDialog.Builder(
                                activity!!
                            )
                            alertDialogBuilder.setView(promptsView)
                            // dialog error tidak dapat di tutup selain menekan tombol close
                            alertDialogBuilder.setCancelable(true)
                            val alertDialog = alertDialogBuilder.create()
                            val back = ColorDrawable(Color.TRANSPARENT)
                            val inset = InsetDrawable(back, 20)
                            alertDialog.window!!.setBackgroundDrawable(inset)

                            val cardView = promptsView.findViewById<View>(R.id.card_dialog_quiz) as MaterialCardView
                            cardView.strokeColor = ContextCompat.getColor(activity!!, R.color.dark_tosca)
                            cardView.strokeWidth = 5

                            val dividerVertical = promptsView.findViewById<View>(R.id.divider_vertical) as View
                            dividerVertical.setBackgroundColor(resources.getColor(R.color.dark_tosca))

                            val dividerHorizontal = promptsView.findViewById<View>(R.id.divider_horizontal) as View
                            dividerHorizontal.setBackgroundColor(resources.getColor(R.color.dark_tosca))

                            // panggil textview judul error dari layout dialog
                            val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_quiz) as TextView
                            titleView.text = (resources.getString(R.string.take_quiz_psr))

                            // panggil gambar close error dari layout dialog
                            val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_dialog_quiz) as ImageView
                            ivDismiss.setColorFilter(context!!.resources.getColor(R.color.dark_tosca))
                            ivDismiss.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            val noTake = promptsView.findViewById<View>(R.id.tv_no_take_quiz) as TextView
                            noTake.setOnClickListener {
                                alertDialog.dismiss()
                            }

                            val takeQuiz = promptsView.findViewById<View>(R.id.tv_take_quiz) as TextView
                            takeQuiz.setTextColor(context!!.resources.getColor(R.color.dark_tosca))
                            takeQuiz.setOnClickListener {
                                startActivity(Intent(activity!!, IntroPeakStateQuizActivity::class.java)) // pindah ke login
                                Animatoo.animateSwipeLeft(activity!!)
                            }
                            alertDialog.show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            true
        }

    private fun onPreferenceFeedback(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {

            true
        }

    private fun onPreferenceShare(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            val appPackageName = activity!!.packageName
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "\n\n For more weather updates, check this cool Weather app at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            this.startActivity(sendIntent)

            true
        }

    private fun onPreferenceLogout(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            // buat dialog logout
            val li = LayoutInflater.from(activity)
            // panggil layout dialog logout
            val promptsView: View = li.inflate(R.layout.dialog_logout, null)
            val alertDialogBuilder = AlertDialog.Builder(
                activity
            )
            alertDialogBuilder.setView(promptsView)
            // dialog error tidak dapat di tutup selain menekan tombol close
            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, 20)
            alertDialog.window!!.setBackgroundDrawable(inset)
            // panggil textview desc error dari layout dialog
            val tvNo = promptsView.findViewById<View>(R.id.tv_no_logout) as TextView
            alertDialog.show()
            tvNo.setOnClickListener {
                alertDialog.dismiss()
            }

            val tvYes = promptsView.findViewById<View>(R.id.tv_yes_logout) as TextView
            tvYes.setOnClickListener {
                auth.signOut() // fungsi dari firebase auth untuk logout
                startActivity(Intent(context, AuthActivity::class.java)) // pindah ke login
                Animatoo.animateSlideUp(context!!)
                Toast.makeText(context, "Success Logout", Toast.LENGTH_SHORT)
                    .show() // menambah toast

                mGoogleSignInClient.signOut().addOnCompleteListener {
                    startActivity(Intent(context, AuthActivity::class.java)) // pindah ke login
                    Animatoo.animateSlideUp(context!!)
                }
            }

            val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_logout) as ImageView
            ivDismiss.setOnClickListener {
                alertDialog.dismiss()
            }
            true
        }


}