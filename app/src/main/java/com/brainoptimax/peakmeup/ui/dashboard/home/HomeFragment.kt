package com.brainoptimax.peakmeup.ui.dashboard.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakmeup.adapter.reminder.HomeReminderAdapter
import com.brainoptimax.peakmeup.adapter.reminder.ReminderClickListener
import com.brainoptimax.peakmeup.model.Reminders
import com.brainoptimax.peakmeup.ui.anchoring.AnchoringActivity
import com.brainoptimax.peakmeup.ui.intro.IntroBreathingActivity
import com.brainoptimax.peakmeup.ui.emotion.EmotionGaugeActivity
import com.brainoptimax.peakmeup.ui.dashboard.profile.ProfileFragment
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakmeup.ui.intro.IntroAnchoringActivity
import com.brainoptimax.peakmeup.ui.intro.IntroEmotionActivity
import com.brainoptimax.peakmeup.ui.intro.IntroValueGoalsActivity
import com.brainoptimax.peakmeup.ui.quiz.QuizActivity
import com.brainoptimax.peakmeup.ui.reminders.AddRemindersActivity
import com.brainoptimax.peakmeup.ui.reminders.DetailReminderActivity
import com.brainoptimax.peakmeup.ui.reminders.ListRemindersActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


open class HomeFragment : Fragment() {

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!

    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var adapter: HomeReminderAdapter
    private lateinit var viewModelReminder: ReminderViewModel
    private lateinit var viewModelAnchoring: AnchoringViewModel
    private lateinit var viewModelValueGoals: ValueGoalsViewModel

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        val refUser = databaseReference.child("Users").child(auth.currentUser!!.uid)

        viewModelReminder = ViewModelProviders.of(this)[ReminderViewModel::class.java]

        preference = Preferences(requireActivity().applicationContext)
        val uidUser = preference.getValues("uid")

        binding.tvName.text = preference.getValues("username")

        val imgUrl = preference.getValues("imgUrl")!!
        if (imgUrl.isEmpty() || imgUrl == "" || imgUrl == "" || imgUrl.isBlank() || imgUrl == "blank"){
            binding.ivAvatarHome.setImageResource(R.drawable.ic_profile)
        }else{
            Glide.with(this)
                .load(imgUrl)
                .into(binding.ivAvatarHome)
        }

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
            viewModelAnchoring = ViewModelProviders.of(this)[AnchoringViewModel::class.java]

            viewModelAnchoring.allAnchoring(uidUser!!)
            viewModelAnchoring.anchroingMutableLiveData.observe(requireActivity()) { anchoring ->
                if (anchoring!!.isEmpty()){
                    val intent = Intent(requireActivity(), IntroAnchoringActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(requireActivity())
                }else{
                    val intent = Intent(requireActivity(), AnchoringActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(requireActivity())
                }
            }
            viewModelAnchoring.databaseErrorAnchroing.observe(
                requireActivity()
            ) { error ->
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoals.setOnClickListener {
            viewModelValueGoals = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

            viewModelValueGoals.allData
            viewModelValueGoals.goalsMutableLiveData.observe(requireActivity()) { goals ->
                if (goals!!.isEmpty()){
                    val intent = Intent(requireActivity(), IntroValueGoalsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(requireActivity())
                }else{
                    val intent = Intent(requireActivity(), ValueGoalsActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(requireActivity())
                }
            }
            viewModelValueGoals.databaseErrorGoals.observe(
                requireActivity()
            ) { error ->
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnNeuro.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle("COMING SOON")
                .setPositiveButton("Ok") { _, _ ->
                }
                .show()
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
        binding.rvHorizontalReminder.hasFixedSize()
        val llm = LinearLayoutManager(requireActivity())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHorizontalReminder.layoutManager = llm

        viewModelReminder.getAllReminders!!.observe(viewLifecycleOwner) { reminderEntities: List<Reminders?>? ->

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

    override fun onDestroy() {
        super.onDestroy()

        fragmentHomeBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentHomeBinding = null
    }


}