package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.reminder.HomeReminderAdapter
import com.brainoptimax.peakstate.adapter.reminder.ReminderClickListener
import com.brainoptimax.peakstate.databinding.FragmentHomeBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.ui.activity.breathing.IntroBreathingActivity
import com.brainoptimax.peakstate.ui.activity.emotion.EmotionGaugeActivity
import com.brainoptimax.peakstate.ui.activity.goals.ValueGoalsActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroAnchoringActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroEmotionActivity
import com.brainoptimax.peakstate.ui.activity.quiz.QuizActivity
import com.brainoptimax.peakstate.ui.activity.reminders.AddRemindersActivity
import com.brainoptimax.peakstate.ui.activity.reminders.DetailReminderActivity
import com.brainoptimax.peakstate.ui.activity.reminders.ListRemindersActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.Preferences
import com.brainoptimax.peakstate.viewmodel.bottomnav.HomeViewModel
import com.brainoptimax.peakstate.viewmodel.reminder.ReminderViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {


    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeReminderAdapter
    private lateinit var viewModelReminder: ReminderViewModel

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        val refUser = databaseReference.child("Users").child(auth.currentUser!!.uid)

        viewModelReminder = ViewModelProviders.of(this)[ReminderViewModel::class.java]

        binding.ivAvatarHome.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction().also { fragmentTransaction ->
                fragmentTransaction?.replace(R.id.fragment_container, ProfileFragment())?.commit()
            }

            // ubah icon ke icon home
            val bottomNavigationView: BottomNavigationView =
                activity?.findViewById(R.id.navigation) as BottomNavigationView
            bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true
        }

        binding.llReminders.setOnClickListener {
            val intent = Intent(requireActivity(), ListRemindersActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())
        }

        binding.tvMoreRemainder.setOnClickListener {
            val intent = Intent(requireActivity(), ListRemindersActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())

        }

        binding.llEmotion.setOnClickListener {
            val cutoff: Long = Date().time - TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)

            Date(cutoff)
            val dateFormatGmt = SimpleDateFormat("yyyy-MM-dd")
            val oldDate: String = dateFormatGmt.format(Date(cutoff))

            refUser.child("Emotion").child("timestamp")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val date = snapshot.value.toString()
                            if (date == oldDate){
                                refUser.child("Emotion").removeValue()
                            }
                            val intent = Intent(activity!!, EmotionGaugeActivity::class.java)
                            startActivity(intent)
                            Animatoo.animateSlideLeft(activity!!)
                        }else{
                            val intent = Intent(activity!!, IntroEmotionActivity::class.java)
                            startActivity(intent)
                            Animatoo.animateSlideLeft(activity!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })


        }

        binding.btnBreathing.setOnClickListener {
            val intent = Intent(requireActivity(), IntroBreathingActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())
        }

        binding.llQuizzes.setOnClickListener {
            val intent = Intent(requireActivity(), QuizActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())
        }

        binding.btnAnchor.setOnClickListener {
            val intent = Intent(requireActivity(), IntroAnchoringActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())


        }

        binding.btnGoals.setOnClickListener {
            val intent = Intent(requireActivity(), ValueGoalsActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())
        }

        preference = Preferences(requireActivity().applicationContext)
        binding.tvName.text = preference.getValues("username")

        val imgUrl = preference.getValues("imgUrl")!!
        if (imgUrl.isEmpty() || imgUrl == "" || imgUrl.equals("") || imgUrl.isBlank() || imgUrl == "blank"){
            binding.ivAvatarHome.setImageResource(R.drawable.ic_profile)
        }else{
            Glide.with(this)
                .load(imgUrl)
                .into(binding.ivAvatarHome)
        }

        updateRecyclerView()
    }


    private val clickListener: ReminderClickListener = object : ReminderClickListener {
        override fun click(reminders: Reminders?) {
            val intent = Intent(requireActivity(), DetailReminderActivity::class.java)
            intent.putExtra(DetailReminderActivity.EXTRA_TITLE, reminders!!.title)
            intent.putExtra(DetailReminderActivity.EXTRA_DESC, reminders.description)
            intent.putExtra(DetailReminderActivity.EXTRA_SUBTITLE, reminders.subtitle)
            intent.putExtra(DetailReminderActivity.EXTRA_DATE, reminders.date)
            intent.putExtra(DetailReminderActivity.EXTRA_TIME, reminders.time)
            startActivity(intent)
        }

        override fun OnLongClick(reminders: Reminders?) {
            val intent = Intent(requireActivity(), AddRemindersActivity::class.java)
            intent.putExtra("updateReminders", reminders)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView() {
        viewModelReminder.getAllReminders!!.observe(requireActivity()) { reminderEntities: List<Reminders?>? ->
            binding.rvHorizontalReminder.setHasFixedSize(true)
            val llm = LinearLayoutManager(requireActivity())
            llm.orientation = LinearLayoutManager.HORIZONTAL
            binding.rvHorizontalReminder.layoutManager = llm

            if (reminderEntities!!.isNotEmpty()) {
                binding.rvHorizontalReminder.visibility = View.VISIBLE
                binding.layoutEmptyRemainderHome.visibility = View.GONE
            } else {
                binding.rvHorizontalReminder.visibility = View.GONE
                binding.layoutEmptyRemainderHome.visibility = View.VISIBLE
            }

            adapter = HomeReminderAdapter(reminderEntities as MutableList<Reminders>, requireActivity(), clickListener)
            binding.rvHorizontalReminder.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

}