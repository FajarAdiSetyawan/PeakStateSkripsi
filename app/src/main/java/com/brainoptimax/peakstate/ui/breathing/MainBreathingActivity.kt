package com.brainoptimax.peakstate.ui.breathing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityMainBreathingBinding
import com.brainoptimax.peakstate.ui.MainActivity
import com.brainoptimax.peakstate.utils.Animatoo
import kotlin.math.hypot
import render.animations.Fade
import render.animations.Render
import render.animations.Slide
import render.animations.Zoom


class MainBreathingActivity : AppCompatActivity(), OnValueChangeListener,
    CustomBreathBottomDialog.BottomSheetListener {

    private var activityMainBreathingBinding: ActivityMainBreathingBinding? = null
    private val binding get() = activityMainBreathingBinding!!

    private var getTimer: String? = null
    private var inhale = 0
    private var exhale = 0
    private var hold = 0
    private var selectedTimer = 0
    private var isRunning = false
    private var timer = false
    private var countDownTimer: CountDownTimer? = null
    private var cdt: CountDownTimer? = null
    private var fbpeAgain = false

    private var f = 0f

    private val animation = Render(this)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBreathingBinding = ActivityMainBreathingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // ganti warna status bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_breathing)

        // layar selalu menyala
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.ivModeBreathing.visibility = View.INVISIBLE

        getTimer = intent.getStringExtra("timer")
        inhale = 4000
        hold = 2000
        exhale = 4000

        if (intent.getStringExtra("inhale") != null) {
            inhale = intent.getStringExtra("inhale")!!.toInt()
        }
        if (intent.getStringExtra("exhale") != null) {
            exhale = intent.getStringExtra("exhale")!!.toInt()
        }
        if (intent.getStringExtra("hold") != null) {
            hold = intent.getStringExtra("hold")!!.toInt()
        }

        selectedTimer = 1000 * 60 * 60 * 60 //initiliazing timer with infinity

        binding.numberPicker.setBackgroundColor(resources.getColor(android.R.color.transparent))    // ubah menjadi transparan
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 30
        binding.numberPicker.setFadingEdgeLength(2147483647)
        binding.numberPicker.isClickable = false   // tidak bisa diclick
        binding.numberPicker.descendantFocusability =
            NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.numberPicker.setFormatter { i ->
            String.format("%02d", i)
        }

        binding.numberPicker.setOnValueChangedListener(this)
        when {
            getTimer != null && getTimer != "" -> {
                if (getTimer!!.toInt() != 0) {
                    selectedTimer = 1000 * 60 * getTimer!!.toInt()
                    binding.numberPicker.value = getTimer!!.toInt()
                }
            }
        }
        f = 1f
        // cek jika inlale/exhale/hold = 0
        when {
            inhale == 0 && exhale == 0 && hold == 0 -> {
                // default value
                inhale = 4000
                exhale = 4000
                hold = 2000
            }
        }

        binding.ivCustom.setOnClickListener {
            val d = CustomBreathBottomDialog()
            d.show(supportFragmentManager, "exampleBottomSheet")
        }

        binding.cardSleep.setOnClickListener {
            inhale = 4000
            exhale = 7000
            hold = 8000
            fbpeAgain = false

            hideMode()

            binding.ivModeBreathing.visibility = View.VISIBLE
            binding.tvModeBreathing.visibility = View.VISIBLE
            binding.ivModeBreathing.setImageResource(R.drawable.ic_night_sleep)
            binding.tvModeBreathing.text = "Breathe to Sleep"

            binding.backMain.visibility = View.VISIBLE
            animationMode()

        }

        binding.cardRelax.setOnClickListener {
            inhale = 5000
            exhale = 5000
            hold = 0
            fbpeAgain = false

            hideMode()

            binding.ivModeBreathing.visibility = View.VISIBLE
            binding.tvModeBreathing.visibility = View.VISIBLE
            binding.ivModeBreathing.setImageResource(R.drawable.ic_relax)
            binding.tvModeBreathing.text = "Breathe to Relax"

            binding.backMain.visibility = View.VISIBLE
            animationMode()

        }

        binding.cardMeditation.setOnClickListener {
            inhale = 2000
            exhale = 2000
            hold = 2000
            fbpeAgain = true

            hideMode()

            binding.ivModeBreathing.visibility = View.VISIBLE
            binding.tvModeBreathing.visibility = View.VISIBLE

            binding.ivModeBreathing.setImageResource(R.drawable.ic_meditation)
            binding.tvModeBreathing.text = "Breathe to Meditation"

            binding.backMain.visibility = View.VISIBLE
            animationMode()

        }

        binding.btnStartBreathing.setOnClickListener {
            if (isRunning) {
                startActivity(Intent(this, CompletedBreathingActivity::class.java))
                Animatoo.animateSlideUp(this)
                finish()

                countDownTimer!!.cancel()
                timer = false
                isRunning = false
            } else {
                goneComponents()

                isRunning = true
                timer = true
                binding.btnStartBreathing.text = "STOP"
                var extrahold = 0
                if (fbpeAgain) {
                    extrahold = hold
                }

                countDownTimer = object : CountDownTimer(
                    selectedTimer.toLong(),
                    (inhale + hold + exhale + extrahold).toLong()
                ) {
                    override fun onTick(l: Long) {
                        cdt = object : CountDownTimer(inhale.toLong(), inhale.toLong()) {
                            override fun onTick(millisUntilFinished: Long) {
                                binding.btnStartBreathing.text = "STOP"
                                binding.tvStartBreathing.text = "Breath in"
                                f = 2f
                                performAnimation(binding.bg2, f, inhale, hold)
                                performAnimation(
                                    binding.bg1,
                                    f - 0.5f,
                                    inhale,
                                    hold
                                )
                            }

                            override fun onFinish() {
                                timer = false
                                cdt = object : CountDownTimer(hold.toLong(), hold.toLong()) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        binding.tvStartBreathing.text = "Hold"
                                    }

                                    override fun onFinish() {
                                        cdt = object :
                                            CountDownTimer(exhale.toLong(), exhale.toLong()) {
                                            override fun onTick(millisUntilFinished: Long) {
                                                f = 1f
                                                binding.tvStartBreathing.text =
                                                    "Breath out"
                                                performAnimation(
                                                    binding.bg2,
                                                    f,
                                                    exhale,
                                                    hold
                                                )
                                                performAnimation(
                                                    binding.bg1,
                                                    f,
                                                    exhale,
                                                    hold
                                                )
                                            }

                                            override fun onFinish() {
                                                if (fbpeAgain) {
                                                    cdt = object : CountDownTimer(
                                                        hold.toLong(),
                                                        hold.toLong()
                                                    ) {
                                                        override fun onTick(l: Long) {
                                                            binding.tvStartBreathing.text =
                                                                "Hold"
                                                        }

                                                        override fun onFinish() {}
                                                    }.start()
                                                }
                                                timer = false
                                            }
                                        }.start()
                                    }
                                }.start()
                            }
                        }.start()
                    }

                    override fun onFinish() {
                        val lock = Object()
                        try {
                            synchronized(lock) {
                                lock.wait(1000)
                            }
                        } catch (e: Exception) {
                        }
                        val complete = Intent(
                            this@MainBreathingActivity,
                            CompletedBreathingActivity::class.java
                        )
                        startActivity(complete)
                        Animatoo.animateFade(this@MainBreathingActivity)
                        finish()
                    }
                }.start()
            }
        }

        binding.backMain.setOnClickListener {
            animation.setAnimation(Slide.OutDown(binding.tvModeBreathing))
            animation.start()
            animation.setAnimation(Slide.OutUp(binding.ivModeBreathing))
            animation.start()
            animation.setAnimation(Fade.In(binding.cardRelax))
            animation.start()
            animation.setAnimation(Fade.In(binding.cardMeditation))
            animation.start()
            animation.setAnimation(Fade.In(binding.cardSleep))
            animation.start()
            animation.setAnimation(Slide.InRight(binding.ivCustom))
            animation.start()
            animation.setAnimation(Slide.OutLeft(binding.backMain))
            animation.start()
            binding.cardMeditation.visibility = View.VISIBLE
            binding.cardRelax.visibility = View.VISIBLE
            binding.cardSleep.visibility = View.VISIBLE
        }

    }

    private fun hideMode(){
        animation.setAnimation(Slide.InUp(binding.tvModeBreathing))
        animation.start()
        animation.setAnimation(Fade.Out(binding.cardRelax))
        animation.start()
        animation.setAnimation(Fade.Out(binding.cardMeditation))
        animation.start()
        animation.setAnimation(Fade.Out(binding.cardSleep))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.ivCustom))
        animation.start()
        animation.setAnimation(Slide.InLeft(binding.backMain))
        animation.start()
    }

    private fun goneComponents() {
        animation.setAnimation(Fade.Out(binding.cardRelax))
        animation.start()
        animation.setAnimation(Fade.Out(binding.cardMeditation))
        animation.start()
        animation.setAnimation(Slide.OutUp(binding.ivModeBreathing))
        animation.start()
        animation.setAnimation(Fade.Out(binding.cardSleep))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.ivCustom))
        animation.start()
        animation.setAnimation(Slide.OutRight(binding.tvMin))
        animation.start()
        animation.setAnimation(Slide.OutLeft(binding.tvTimer))
        animation.start()
        animation.setAnimation(Slide.OutLeft(binding.tvTimer))
        animation.start()
        animation.setAnimation(Slide.OutLeft(binding.backMain))
        animation.start()
        animation.setAnimation(Zoom.Out(binding.numberPicker))
        animation.start()

    }

    override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {
        for (c in 1..30) {
            if (c == p2) {
                selectedTimer = 1000 * 60 * p1
            }
        }
    }

    override fun onButtonClicked(inhale: Int, exhale: Int, hold: Int) {

        this.inhale = inhale
        this.exhale = exhale
        this.hold = hold
        fbpeAgain = false
    }

    override fun onInhaleProgressChanged(inhale: Int) {
        if (inhale != 0) {
            this.inhale = inhale
        }
    }

    override fun onExhaleProgressChanged(exhale: Int) {
        if (exhale != 0) {
            this.exhale = exhale
        }
    }

    override fun onHoldProgressChanged(hold: Int) {
        if (hold != 0) {
            this.hold = hold
        }
    }

    private fun animationMode() {
        val cx: Int = binding.ivModeBreathing.width / 2
        val cy: Int = binding.ivModeBreathing.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(
            binding.ivModeBreathing,
            cx,
            cy,
            0f,
            finalRadius
        )
        anim.duration = 1000
        anim.start()
    }

    fun performAnimation(im: ImageView?, f: Float, timer: Int, hold: Int) {
        val scaleDownX = ObjectAnimator.ofFloat(im, "scaleX", f)
        val scaleDownY = ObjectAnimator.ofFloat(im, "scaleY", f)
        scaleDownX.duration = timer.toLong()
        scaleDownY.duration = timer.toLong()
        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        //scaleDown.setStartDelay(hold)
        scaleDown.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        Animatoo.animateSwipeLeft(this)
        finish()
    }
}