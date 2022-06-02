package com.brainoptimax.peakstate.ui.reminders

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityDetailReminderBinding
import render.animations.Fade
import render.animations.Render
import render.animations.Slide


class DetailReminderActivity : AppCompatActivity() {

    private var activityDetailReminderBinding: ActivityDetailReminderBinding? = null
    private val binding get() = activityDetailReminderBinding!!

    private val animation = Render(this)

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_SUBTITLE = "extra_subtitle"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_TIME = "extra_time"
    }

    @SuppressLint("SimpleDateFormat", "NewApi", "WeekBasedYear", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailReminderBinding = ActivityDetailReminderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val title = intent.getStringExtra(EXTRA_TITLE)
        val subtitle = intent.getStringExtra(EXTRA_SUBTITLE)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val time = intent.getStringExtra(EXTRA_TIME)
        val date = intent.getStringExtra(EXTRA_DATE)

        when (title) {
            "Morning Routine" -> {
                binding.imgBackdrop.setImageResource(R.drawable.ic_morning)
            }
            "Night Routine" -> {
                binding.imgBackdrop.setImageResource(R.drawable.ic_night)
            }
            "Movement" -> {
                binding.imgBackdrop.setImageResource(R.drawable.ic_movement)
            }
            "Fresh Air" -> {
                binding.imgBackdrop.setImageResource(R.drawable.ic_fresh)
            }
            else -> {
                binding.imgBackdrop.setImageResource(R.drawable.ic_placeholder_image)
            }
        }

        animation.setAnimation(Slide.InUp(binding.tvDatetimeRemainder))
        animation.start()
        animation.setAnimation(Fade.In(binding.tvSubtitleRemainder))
        animation.start()
        animation.setAnimation(Fade.In(binding.tvDecsDetailRemainder))
        animation.start()

        binding.toolbar.title = title
        binding.tvSubtitleRemainder.text = subtitle
        binding.tvDecsDetailRemainder.text = desc
        binding.tvDatetimeRemainder.text = "$date $time"
    }
}