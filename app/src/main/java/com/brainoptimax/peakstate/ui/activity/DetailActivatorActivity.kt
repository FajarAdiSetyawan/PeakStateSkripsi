package com.brainoptimax.peakstate.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityDetailActivatorBinding
import com.brainoptimax.peakstate.model.activator.DetailActivator
import com.brainoptimax.peakstate.ui.activity.anchoring.Anchoring1Activity
import com.brainoptimax.peakstate.ui.activity.breathing.MainBreathingActivity
import com.brainoptimax.peakstate.utils.Animatoo

class DetailActivatorActivity : AppCompatActivity() {

    private var activityDetailActivatorBinding: ActivityDetailActivatorBinding? = null
    private val binding get() = activityDetailActivatorBinding!!

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailActivatorBinding = ActivityDetailActivatorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val desc = intent.getStringExtra(EXTRA_DESC)


        binding.tvTitleDetailActivator.text = title
        binding.tvDescDetailActivator.text = desc

        when {
            title.equals(resources.getString(R.string.physical_calm_alert_state)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #1"
                binding.tvActivatorMusic.text = "Music - Relax #1"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.being_present_relaxed_body_and_mind)
            }
            title.equals(resources.getString(R.string.relax_the_mind_for_creative_pursuits)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #2"
                binding.tvActivatorMusic.text = "Music - Relax #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.relaxed_focus)
            }
            title.equals(resources.getString(R.string.executive_functions)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Energize #1"
                binding.tvActivatorMusic.text = "Music - Energize #1"
                binding.tvActivatorBreath.text = "Breathing - 4-6 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.focused_attentive_state)
            }
            title.equals(resources.getString(R.string.group_work)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Energize #2"
                binding.tvActivatorMusic.text = "Music - Energize #2"
                binding.tvActivatorBreath.text = "Breathing - 3-5 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.confidience_agility)
            }
            title.equals(resources.getString(R.string.high_intensity_activity)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Energize #3"
                binding.tvActivatorMusic.text = "Music - Energize #3"
                binding.tvActivatorBreath.text = "Breathing - 5-6 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.focused_mentally_alert_attentive_state)
            }
            title.equals(resources.getString(R.string.creative_task)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Relax #1"
                binding.tvActivatorMusic.text = "Music - Relax #1"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.being_present_relaxed_body_and_mind)
            }
            title.equals(resources.getString(R.string.study)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Relax #2"
                binding.tvActivatorMusic.text = "Music - Relax #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_worrying_thoughts)
            }
            title.equals(resources.getString(R.string.practice)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Relax #3"
                binding.tvActivatorMusic.text = "Music - Relax #3"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_distractions_being_grounded_in_the_present)
            }
            title.equals(resources.getString(R.string.creative_pursuits)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - BB.V #1"
                binding.tvActivatorMusic.text = "Music - BB.V #1"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.a_relaxed_mind_body)
            }
            title.equals(resources.getString(R.string.skill_improvement_visualization)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - BB.V #2"
                binding.tvActivatorMusic.text = "Music - BB.V #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.mental_effort_in_the_presence_of_a_quiet_emotional_state)
            }
            title.equals(resources.getString(R.string.meditation)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #1"
                binding.tvActivatorMusic.text = "Music - FG #1"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.being_present_relaxed_body_and_mind)
            }
            title.equals(resources.getString(R.string.quiet_contemplation)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #2"
                binding.tvActivatorMusic.text = "Music - FG #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_distractions_being_grounded_in_the_present)
            }
            title.equals(resources.getString(R.string.studying)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #3"
                binding.tvActivatorMusic.text = "Music - FG #3"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.confidence_focused_attention_to_detail)
            }
            title.equals(resources.getString(R.string.calm_relaxation)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - FG #4"
                binding.tvActivatorMusic.text = "Music - FG #4"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_worrying_thoughts)
            }
            title.equals(resources.getString(R.string.sleep_meditate)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Sleep #2"
                binding.tvActivatorMusic.text = "Music - Sleep #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_worrying_thoughts)
            }
            title.equals(resources.getString(R.string.calm)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Sleep #3"
                binding.tvActivatorMusic.text = "Music - Sleep #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_disctraction)
            }
            title.equals(resources.getString(R.string.mental_quiescence)) -> {
                binding.tvActivatorIso.text = "Isochronic tone - Sleep #4"
                binding.tvActivatorMusic.text = "Music - Sleep #2"
                binding.tvActivatorBreath.text = "Breathing - 5-7 BPM"
                binding.tvActivatorSuggest.text = resources.getString(R.string.free_from_worrying_thoughts)
            }
        }

        binding.cardBreath.setOnClickListener {
            startActivity(Intent(this, MainBreathingActivity::class.java))
            Animatoo.animateSlideUp(this)
            finish()
        }

        binding.cardAnchoring.setOnClickListener {
            startActivity(Intent(this, Anchoring1Activity::class.java))
            Animatoo.animateSlideUp(this)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}