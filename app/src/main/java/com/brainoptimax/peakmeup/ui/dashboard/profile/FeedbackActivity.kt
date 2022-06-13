package com.brainoptimax.peakmeup.ui.dashboard.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityFeedbackBinding


class FeedbackActivity : AppCompatActivity() {

    private var activityFeedbackBinding: ActivityFeedbackBinding? = null
    private val binding get() = activityFeedbackBinding!!

    private var report = ""
    var empty = 0
    private var radioButtonExp: RadioButton? = null

    private lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityFeedbackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Shared Preferences
        preferences = Preferences(this)

        binding.btnSend.setOnClickListener {
            sendFeedback()
        }

    }

    fun onCheckboxClicked(view: View) {
        when (view.id) {
            R.id.cb_error -> {
                binding.cbBug.isChecked = false
                binding.cbOther.isChecked = false
            }
            R.id.cb_other -> {
                binding.cbBug.isChecked = false
                binding.cbError.isChecked = false
            }
            R.id.cb_bug -> {
                binding.cbError.isChecked = false
                binding.cbOther.isChecked = false
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun sendFeedback() {
        val selectedIdExp: Int = binding.rgExperience.checkedRadioButtonId

        if (binding.rgExperience.checkedRadioButtonId == -1) {
            // no radio buttons are checked
            Toast.makeText(
                this@FeedbackActivity, "Please Select your Experience", Toast.LENGTH_SHORT
            ).show()
        } else {
            radioButtonExp = findViewById<View>(selectedIdExp) as RadioButton
            val experienceSelected = radioButtonExp!!.text.toString()
            val descFeedback = binding.etExperience.text.toString().trim()

            if (binding.cbBug.isChecked) {
                report = binding.cbBug.text.toString()
                empty = 1
            }

            if (binding.cbOther.isChecked) {
                report = binding.cbOther.text.toString()
                empty = 1
            }
            if (binding.cbError.isChecked) {
                report = binding.cbError.text.toString()
                empty = 1
            }

            if (empty == 0) {
                Toast.makeText(
                    this@FeedbackActivity,
                    "Please Select your Feedback",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                when {
                    descFeedback.isEmpty() -> {
                        Toast.makeText(
                            this@FeedbackActivity,
                            "Description Experience Not Blank",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val username = preferences.getValues("username")
                        val feedbackMsg = "" +
                                "From Username  : $username \n" +
                                "Experience     : $experienceSelected \n " +
                                "Report         : $report \n " +
                                "Description    : $descFeedback"


                        val email = arrayOf("fajaras465@gmail.com")

                        //provide email address of the recipient as data
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
                        emailIntent.putExtra(Intent.EXTRA_TEXT, feedbackMsg)
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback  " + resources.getString(R.string.app_name)
                        )
                        emailIntent.type = "application/image"
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                    }
                }
            }

        }
    }

}