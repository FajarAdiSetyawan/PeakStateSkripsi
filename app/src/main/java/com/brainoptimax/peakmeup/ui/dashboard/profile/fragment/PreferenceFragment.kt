package com.brainoptimax.peakmeup.ui.dashboard.profile.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.brainoptimax.peakmeup.ui.auth.AuthActivity
import com.brainoptimax.peakmeup.ui.dashboard.profile.AboutActivity
import com.brainoptimax.peakmeup.ui.dashboard.profile.EditProfileActivity
import com.brainoptimax.peakmeup.ui.dashboard.profile.FeedbackActivity
import com.brainoptimax.peakmeup.ui.intro.IntroEnergyTensionActivity
import com.brainoptimax.peakmeup.ui.intro.IntroPeakStateQuizActivity
import com.brainoptimax.peakmeup.ui.quiz.QuizActivity
import com.brainoptimax.peakmeup.utils.*
import com.brainoptimax.peakmeup.viewmodel.quiz.QuizViewModel
import com.brainoptimax.peakmeup.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PreferenceFragment : PreferenceFragmentCompat(), OnLocaleChangedListener {

    private lateinit var prefTheme: ListPreference
    private lateinit var prefLang: Preference
    private lateinit var prefProfile: Preference
    private lateinit var prefEnergy: Preference
    private lateinit var prefPeak: Preference
    private lateinit var prefShare: Preference
    private lateinit var prefFeedback: Preference
    private lateinit var prefLogout: Preference
    private lateinit var prefAbout: Preference

    private lateinit var preferences: Preferences

    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var viewModelQuiz: QuizViewModel

    private var localizationAgent: LocalizationAgent? = null

    var uid = ""

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // memanggil layout pref_main
        addPreferencesFromResource(R.xml.pref_main)
        initComponents()

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid)

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        localizationAgent = LocalizationAgent(requireActivity())
        localizationAgent?.addOnLocaleChangedListener(this)
        localizationAgent?.onCreate()

        viewModelQuiz = ViewModelProviders.of(this)[QuizViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Shared Preferences
        preferences = Preferences(requireActivity())
        uid = preferences.getValues("uid")!!
    }

    override fun onResume() {
        super.onResume()
        localizationAgent?.onResume()
    }

    private fun setLanguage(language: String) {
        localizationAgent?.language = language
    }


    override fun beforeLocaleChanged() = Unit
    override fun afterLocaleChanged() = Unit

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

        prefAbout = findPreference<Preference>(getString(R.string.pref_key_about))
                as Preference
        prefAbout.onPreferenceClickListener = onPreferenceAbout()

        prefLang = findPreference<Preference>(getString(R.string.pref_key_lang))
                as Preference
        prefLang.onPreferenceClickListener = onLangPreferenceChange()

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

    private fun onLangPreferenceChange(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {

            val dialogBuilder = AlertDialog.Builder(requireActivity())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.language_dialog, null)
            dialogBuilder.setView(dialogView)

            val spinner1: AppCompatSpinner = dialogView!!.findViewById(R.id.spinner1)

            dialogBuilder.setTitle(resources.getString(R.string.language))
            dialogBuilder.setPositiveButton(getString(R.string.change), DialogInterface.OnClickListener { _, _ ->
                when (spinner1.selectedItemPosition) {
                    1 //Indonesia
                    -> {
                        setLanguage(LanguageSetting.LANGUAGE_INDONESIA)
                        return@OnClickListener
                    }
                    else //By default set to english

                    -> {
                        setLanguage(LanguageSetting.LANGUAGE_ENGLISH)
                        return@OnClickListener
                    }
                }
            })
            dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }
            val b = dialogBuilder.create()
            b.show()
            true
        }



    private fun onPreferenceProfile(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java)) // pindah ke login
            Animatoo.animateSwipeLeft(requireActivity())
            true
        }

    private fun onPreferenceAbout(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            startActivity(Intent(requireActivity(), AboutActivity::class.java)) // pindah ke login
            Animatoo.animateSwipeLeft(requireActivity())
            true
        }

    private fun onPreferenceEnergy(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            // mengambil data hasil quiz energi tension
            viewModelQuiz.getResultEnergy(uid)
            viewModelQuiz.energyQuizMutableLiveData.observe(viewLifecycleOwner){ energy ->
                if (energy!!.isEmpty() || energy.equals(null) || energy.equals("null")){
                    // buat dialog error internet
                    val li = LayoutInflater.from(requireActivity())
                    // panggil layout dialog error
                    val promptsView: View = li.inflate(R.layout.dialog_quiz, null)
                    val alertDialogBuilder = AlertDialog.Builder(
                        requireActivity()
                    )
                    alertDialogBuilder.setView(promptsView)
                    // dialog error tidak dapat di tutup selain menekan tombol close
                    alertDialogBuilder.setCancelable(true)
                    val alertDialog = alertDialogBuilder.create()
                    val back = ColorDrawable(Color.TRANSPARENT)
                    val inset = InsetDrawable(back, 20)
                    alertDialog.window!!.setBackgroundDrawable(inset)

                    val cardView = promptsView.findViewById<View>(R.id.card_dialog_quiz) as MaterialCardView
                    cardView.strokeColor = ContextCompat.getColor(requireActivity(), R.color.md_orange_400)
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
                        startActivity(Intent(requireActivity(), IntroEnergyTensionActivity::class.java)) // pindah ke login
                        Animatoo.animateSwipeLeft(requireActivity())
                    }
                    alertDialog.show()
                }else{
                    startActivity(Intent(requireActivity(), QuizActivity::class.java)) // pindah ke login
                    Animatoo.animateSwipeLeft(requireActivity())
                }
            }

            true
        }

    private fun onPreferencePeak(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            viewModelQuiz.getResultPeak(uid)
            viewModelQuiz.peakQuizMutableLiveData.observe(viewLifecycleOwner){ peak ->
                if (peak!!.isEmpty() || peak.equals(null) || peak.equals("null")){
                    // buat dialog error internet
                    val li = LayoutInflater.from(requireActivity())
                    // panggil layout dialog error
                    val promptsView: View = li.inflate(R.layout.dialog_quiz, null)
                    val alertDialogBuilder = AlertDialog.Builder(
                        requireActivity()
                    )
                    alertDialogBuilder.setView(promptsView)
                    // dialog error tidak dapat di tutup selain menekan tombol close
                    alertDialogBuilder.setCancelable(true)
                    val alertDialog = alertDialogBuilder.create()
                    val back = ColorDrawable(Color.TRANSPARENT)
                    val inset = InsetDrawable(back, 20)
                    alertDialog.window!!.setBackgroundDrawable(inset)

                    val cardView = promptsView.findViewById<View>(R.id.card_dialog_quiz) as MaterialCardView
                    cardView.strokeColor = ContextCompat.getColor(requireActivity(), R.color.dark_tosca)
                    cardView.strokeWidth = 5

                    val dividerVertical = promptsView.findViewById(R.id.divider_vertical) as View
                    dividerVertical.setBackgroundColor(resources.getColor(R.color.dark_tosca))

                    val dividerHorizontal = promptsView.findViewById(R.id.divider_horizontal) as View
                    dividerHorizontal.setBackgroundColor(resources.getColor(R.color.dark_tosca))

                    // panggil textview judul error dari layout dialog
                    val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_quiz) as TextView
                    titleView.text = (resources.getString(R.string.take_quiz_psr))

                    // panggil gambar close error dari layout dialog
                    val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_dialog_quiz) as ImageView
                    ivDismiss.setColorFilter(requireContext().resources.getColor(R.color.dark_tosca))
                    ivDismiss.setOnClickListener {
                        alertDialog.dismiss()
                    }

                    val noTake = promptsView.findViewById<View>(R.id.tv_no_take_quiz) as TextView
                    noTake.setOnClickListener {
                        alertDialog.dismiss()
                    }

                    val takeQuiz = promptsView.findViewById<View>(R.id.tv_take_quiz) as TextView
                    takeQuiz.setTextColor(requireContext().resources.getColor(R.color.dark_tosca))
                    takeQuiz.setOnClickListener {
                        startActivity(Intent(requireActivity(), IntroPeakStateQuizActivity::class.java)) // pindah ke login
                        Animatoo.animateSwipeLeft(requireActivity())
                    }
                    alertDialog.show()
                }else{
                    startActivity(Intent(requireActivity(), QuizActivity::class.java)) // pindah ke login
                    Animatoo.animateSwipeLeft(requireActivity())
                }
            }
            true
        }

    private fun onPreferenceFeedback(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            startActivity(Intent(context, FeedbackActivity::class.java)) // pindah ke login
            Animatoo.animateSwipeRight(requireContext())
            true
        }

    private fun onPreferenceShare(): Preference.OnPreferenceClickListener =
        Preference.OnPreferenceClickListener {
            val appPackageName = requireActivity().packageName
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "\n\n Lorem Ipsum, check this cool app at: https://play.google.com/store/apps/details?id=$appPackageName"
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
                Animatoo.animateSlideUp(requireContext())
                Toast.makeText(context, resources.getString(R.string.success_logout), Toast.LENGTH_SHORT)
                    .show() // menambah toast

                mGoogleSignInClient.signOut().addOnCompleteListener {
                    startActivity(Intent(context, AuthActivity::class.java)) // pindah ke login
                    Animatoo.animateSlideUp(requireContext())
                }


                // menghapus preference data user yg login
                preferences.toLogout()

            }

            val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_logout) as ImageView
            ivDismiss.setOnClickListener {
                alertDialog.dismiss()
            }
            true
        }


}