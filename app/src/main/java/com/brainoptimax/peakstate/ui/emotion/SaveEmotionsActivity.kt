package com.brainoptimax.peakstate.ui.emotion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivitySaveEmotionsBinding
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.viewmodel.emotion.EmotionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class SaveEmotionsActivity : AppCompatActivity() {
    private var activitySaveEmotionsBinding: ActivitySaveEmotionsBinding? = null
    private val binding get() = activitySaveEmotionsBinding!!

    private var emotionsSelected: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var viewModel: EmotionViewModel

    companion object {
        const val EXTRA_EMOTION = "extra_emotion"
        const val EXTRA_CONDITION = "extra_condition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySaveEmotionsBinding = ActivitySaveEmotionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProviders.of(this)[EmotionViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val refEmotion =
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")

        val emotions = intent.getStringExtra(EXTRA_EMOTION)
        val condition = intent.getStringExtra(EXTRA_CONDITION)

        binding.tvEmotion.text = emotions

        viewModel.totalAllEmotion
        viewModel.totalAllEmotionMutableLiveData.observe(this) { totalAllEmotion ->
            Log.d("TAG", "totalAllEmotion: $totalAllEmotion")

            if (totalAllEmotion!!.isEmpty() || totalAllEmotion.equals(null) || totalAllEmotion == "null") {
                binding.tvTotal.text = "0"
            } else {
                binding.tvTotal.text = totalAllEmotion
            }
        }

        when (emotions) {
            resources.getString(R.string.excited) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_exited)
                emotionsSelected = "Excited"
            }
            resources.getString(R.string.enthusiastic) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_enthusiastic)
                emotionsSelected = "Enthusiastic"
            }
            resources.getString(R.string.happy) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_happy)
                emotionsSelected = "Happy"
            }
            resources.getString(R.string.grateful) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_grateful)
                emotionsSelected = "Grateful"
            }
            resources.getString(R.string.passionate) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_passionate)
                emotionsSelected = "Passionate"
            }
            resources.getString(R.string.joyful) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_joyful)
                emotionsSelected = "Joyful"
            }
            resources.getString(R.string.brave) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_brave)
                emotionsSelected = "Brave"
            }
            resources.getString(R.string.confident) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_confident)
                emotionsSelected = "Confident"
            }
            resources.getString(R.string.proud) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_proud)
                emotionsSelected = "Proud"
            }
            resources.getString(R.string.hopeful) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_hopeful)
                emotionsSelected = "Hopeful"
            }
            resources.getString(R.string.optimistic) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_optimistic)
                emotionsSelected = "Optimistic"
            }
            resources.getString(R.string.calm) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_calm)
                emotionsSelected = "Calm"
            }
            resources.getString(R.string.`fun`) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_fun)
                emotionsSelected = "Fun"
            }
            resources.getString(R.string.awful) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_awful)
                emotionsSelected = "Awful"
            }
            resources.getString(R.string.good) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_good)
                emotionsSelected = "Good"
            }
            resources.getString(R.string.numb) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_numb)
                emotionsSelected = "Numb"
            }
            resources.getString(R.string.bad) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_bad)
                emotionsSelected = "Bad"
            }
            resources.getString(R.string.bored) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_bored)
                emotionsSelected = "Bored"
            }
            resources.getString(R.string.tired) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_tired)
                emotionsSelected = "Tired"
            }
            resources.getString(R.string.frustrated) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_frustrated)
                emotionsSelected = "Frustrated"
            }
            resources.getString(R.string.stressed) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_stressed)
                emotionsSelected = "Stressed"
            }
            resources.getString(R.string.insecure) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_insecure)
                emotionsSelected = "Insecure"
            }
            resources.getString(R.string.angry) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_angry)
                emotionsSelected = "Angry"
            }
            resources.getString(R.string.sad) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_sad)
                emotionsSelected = "Sad"
            }
            resources.getString(R.string.afraid) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_afraid)
                emotionsSelected = "Afraid"
            }
            resources.getString(R.string.envious) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_envious)
                emotionsSelected = "Envious"
            }
            resources.getString(R.string.anxious) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_anxious)
                emotionsSelected = "Anxious"
            }
            resources.getString(R.string.down) -> {
                binding.ivEmotion.setImageResource(R.drawable.ic_down)
                emotionsSelected = "Down"
            }
        }

        viewModel.totalPerEmotion(condition!!, emotionsSelected!!)
        viewModel.totalPerEmotionMutableLiveData.observe(this) { totalPerEmotion ->
            Log.d("TAG", "totalPerEmotion: $totalPerEmotion")
            if (totalPerEmotion!!.isEmpty() || totalPerEmotion.equals(null) || totalPerEmotion == "null") {
                binding.tvTotalEmotion.text = "0"
            } else {
                binding.tvTotalEmotion.text = totalPerEmotion
            }
        }

        val sdf = SimpleDateFormat("E, d MMMM yyyy - H:mm ", Locale.getDefault())
        val currentDateTime = sdf.format(Date())
        val sdf2 = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val dateFormat = sdf2.format(Date())
        val sdf3 = SimpleDateFormat("H:mm:ss", Locale.getDefault())
        val timeFormat = sdf3.format(Date())

        binding.tvDatetimeEmotion.text = currentDateTime


        binding.btnSaveEmotion.setOnClickListener {
            val emotionNote = binding.etNote.text.toString().trim()

            var totalAllEmotion = binding.tvTotal.text.toString().toInt()
            totalAllEmotion++

            var totalPerEmotion = binding.tvTotalEmotion.text.toString().toInt()
            totalPerEmotion++

            when {
                emotionNote.isEmpty() -> {
                    binding.etNote.error = resources.getString(R.string.note_blank)
                    binding.etNote.requestFocus()
                    Toast.makeText(
                        this,
                        resources.getString(R.string.note_blank),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                totalAllEmotion == 0 ->{

                }
                totalPerEmotion == 0 ->{

                }
                else -> {
                    binding.etNote.error = null

                    MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialogRounded)
                        .setTitle(resources.getString(R.string.confirm_action))
                        .setMessage(resources.getString(R.string.desc_dialog_add_emotion))
                        .setPositiveButton("Ok") { _, _ ->
                            refEmotion.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    when {
                                        dataSnapshot.exists() -> {
                                            viewModel.addEmotion(
                                                refEmotion,
                                                view,
                                                totalAllEmotion,
                                                totalPerEmotion,
                                                condition,
                                                emotionsSelected!!,
                                                emotionNote,
                                                currentDateTime,
                                                dateFormat,
                                                timeFormat
                                            )

                                            intentToMain()
                                        }
                                        else -> {
                                            refEmotion.child("timestamp")
                                                .setValue(System.currentTimeMillis())
                                                .addOnSuccessListener {
                                                    viewModel.addEmotion(
                                                        refEmotion,
                                                        view,
                                                        totalAllEmotion,
                                                        totalPerEmotion,
                                                        condition,
                                                        emotionsSelected!!,
                                                        emotionNote,
                                                        currentDateTime,
                                                        dateFormat,
                                                        timeFormat
                                                    )
                                                    intentToMain()
                                                }.addOnFailureListener {
                                                Toast.makeText(
                                                    applicationContext, "Emotion Not Recorded",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@SaveEmotionsActivity,
                                        "Error " + error.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                        .setNegativeButton(
                            resources.getString(R.string.cancel)
                        ) { dialog, which -> }
                        .show()
                }
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ChooseEmotionsActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }

    private fun intentToMain() {
        Toast.makeText(this, resources.getString(R.string.emotion_recorded), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, EmotionGaugeActivity::class.java))
        Animatoo.animateSlideDown(this)
        finish()
    }
}