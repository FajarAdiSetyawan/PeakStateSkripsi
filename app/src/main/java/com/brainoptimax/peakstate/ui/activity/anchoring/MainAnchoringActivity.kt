package com.brainoptimax.peakstate.ui.activity.anchoring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityMainAnchoringBinding

class MainAnchoringActivity : AppCompatActivity() {

    private var activityMainAnchoringBinding: ActivityMainAnchoringBinding? = null
    private val binding get() = activityMainAnchoringBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainAnchoringBinding = ActivityMainAnchoringBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }
}