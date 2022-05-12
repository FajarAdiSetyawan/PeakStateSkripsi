package com.brainoptimax.peakstate.ui.activity.emotion

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.ActivityResultEmotionBinding
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultEmotionActivity : AppCompatActivity() {

    private var activityResultEmotionBinding: ActivityResultEmotionBinding? = null
    private val binding get() = activityResultEmotionBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.md_yellow_600)

        activityResultEmotionBinding = ActivityResultEmotionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        setupViewPager(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.btnRecomendation.setOnClickListener {
            startActivity(Intent(this, RecommendationEmotionActivity::class.java))
            Animatoo.animateCard(this)
            finish()
        }

        binding.ivClose.setOnClickListener {
            startActivity(Intent(this, EmotionGaugeActivity::class.java))
            Animatoo.animateSlideDown(this)
            finish()
        }

        val refEmotion = databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")
        refEmotion.child("total")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val total = snapshot.value.toString()
                        binding.tvTotal.text = "Total $total Emotion"
                    } else {
                        binding.tvTotal.text = "0 Total Emotion"
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ResultEmotionActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(ResultPositiveFragment(), resources.getString(R.string.positive))
        adapter.addFrag(ResultNegativeFragment(), resources.getString(R.string.negative))
        viewPager.adapter = adapter
    }

    internal class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}