package com.brainoptimax.peakstate.ui.activity.emotion

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.emotions.DailyEmotionAdapter
import com.brainoptimax.peakstate.databinding.FragmentDailyEmotionBinding
import com.brainoptimax.peakstate.model.Emotion
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class DailyEmotionFragment : Fragment(), DatePickerListener {
    private var fragmentDailyEmotionBinding: FragmentDailyEmotionBinding? = null
    private val binding get() = fragmentDailyEmotionBinding!!

    private lateinit var emotionsList: ArrayList<Emotion>
    lateinit var emotionDayAdapter: DailyEmotionAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDailyEmotionBinding =
            FragmentDailyEmotionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.rvEmotion.layoutManager = layoutManager


        // find the picker
        val picker = binding.datePicker
        // initialize it and attach a listener
        picker.setListener(this)
            .setDays(14)
            .setOffset(7)
            .setDateSelectedColor(resources.getColor(R.color.md_yellow_600))
            .setDateSelectedTextColor(Color.WHITE)
            .setMonthAndYearTextColor(Color.DKGRAY)
            .setTodayButtonTextColor(resources.getColor(R.color.colorPrimary))
            .setTodayDateTextColor(resources.getColor(R.color.white))
            .setTodayDateBackgroundColor(Color.GRAY)
            .setUnselectedDayTextColor(Color.DKGRAY)
            .setDayOfWeekTextColor(Color.DKGRAY)
            .setUnselectedDayTextColor(resources.getColor(R.color.md_blue_grey_200))
            .showTodayButton(true)
            .init()
        picker.backgroundColor = resources.getColor(R.color.bg_layout)
        picker.setDate(DateTime().plusDays(0))
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSelected(dateSelected: DateTime?) {
        //merubah format tanggal
        val result = dateSelected!!.toDate()
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy")
        val day = sdf.format(result)

        binding.progressBarEmotion.visibility = View.VISIBLE
        // read database -> users -> currentid -> emotion -> daily -> day
        val refEmotion =
            databaseReference.child("Users").child(auth.currentUser!!.uid).child("Emotion")
        refEmotion.child("daily").child(day)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // show all data
                        emotionsList = arrayListOf()
                        for (item in dataSnapshot.children) {
                            val emotion = item.getValue(Emotion::class.java)
                            emotionsList.add(emotion!!)
                        }
                        binding.layoutEmpty.visibility = View.INVISIBLE
                        binding.rvEmotion.visibility = View.VISIBLE
                        binding.rvEmotion.adapter = DailyEmotionAdapter(emotionsList)
                    } else {
                        binding.layoutEmpty.visibility = View.VISIBLE
                        binding.rvEmotion.visibility = View.INVISIBLE
                    }
                    binding.progressBarEmotion.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }


}

