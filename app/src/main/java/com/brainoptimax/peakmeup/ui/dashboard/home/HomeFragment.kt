package com.brainoptimax.peakmeup.ui.dashboard.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.adapter.reminder.HomeReminderAdapter
import com.brainoptimax.peakmeup.ui.anchoring.AnchoringActivity
import com.brainoptimax.peakmeup.ui.intro.IntroBreathingActivity
import com.brainoptimax.peakmeup.ui.emotion.EmotionGaugeActivity
import com.brainoptimax.peakmeup.ui.dashboard.profile.ProfileFragment
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakmeup.ui.intro.IntroAnchoringActivity
import com.brainoptimax.peakmeup.ui.intro.IntroEmotionActivity
import com.brainoptimax.peakmeup.ui.intro.IntroValueGoalsActivity
import com.brainoptimax.peakmeup.ui.quiz.QuizActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.anchoring.AnchoringViewModel
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.reminder.ReminderAdapter
import com.brainoptimax.peakmeup.databinding.FragmentHomeBinding
import com.brainoptimax.peakmeup.ui.reminders.ReminderActivity
import com.brainoptimax.peakmeup.viewmodel.emotion.EmotionViewModel
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
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

    private lateinit var adapter: HomeReminderAdapter
    private lateinit var viewModelReminder: ReminderViewModel
    private lateinit var viewModelAnchoring: AnchoringViewModel
    private lateinit var viewModelValueGoals: ValueGoalsViewModel
    private lateinit var viewModelEmotion: EmotionViewModel

    private lateinit var preference: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "UseRequireInsteadOfGet", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        showLoading()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalReminder.layoutManager = layoutManager
        adapter = HomeReminderAdapter(requireActivity())
        binding.rvHorizontalReminder.adapter = adapter

        viewModelReminder.allReminder(uidUser!!)
        viewModelReminder.allReminderMutableLiveData.observe(viewLifecycleOwner){ reminder ->
            Log.d("TAG", "onDataChangeReminder: $reminder")
            if (reminder!!.isEmpty() || reminder.equals(null) || reminder.equals("null")){
                binding.rvHorizontalReminder.visibility = View.INVISIBLE
                binding.layoutEmptyRemainderHome.visibility = View.VISIBLE
                goneLoading()
            }else{
                binding.rvHorizontalReminder.visibility = View.VISIBLE
                binding.layoutEmptyRemainderHome.visibility = View.INVISIBLE
                goneLoading()
            }
            adapter.setReminder(reminder)
            adapter.notifyDataSetChanged()

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
            val intent = Intent(requireActivity(), ReminderActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())
        }

        binding.tvMoreRemainder.setOnClickListener {
            val intent = Intent(requireActivity(), ReminderActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(requireActivity())

        }

        binding.llEmotion.setOnClickListener {
            viewModelEmotion = ViewModelProviders.of(this)[EmotionViewModel::class.java]
            val cutoff: Long = Date().time - TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)

            Date(cutoff)
            val dateFormatGmt = SimpleDateFormat("yyyy-MM-dd")
            val oldDate: String = dateFormatGmt.format(Date(cutoff))
            viewModelEmotion.currentDate(uidUser)
            viewModelEmotion.currentDateMutableLiveData.observe(viewLifecycleOwner){ timestamp ->
                Log.d("TAG", "timestamp: $timestamp")
                if (timestamp!!.isEmpty() || timestamp.equals(null) || timestamp == "null"){
                    val intent = Intent(activity!!, IntroEmotionActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(activity!!)
                }else{
                    if (timestamp == oldDate){
                        viewModelEmotion.deleteEmotion(uidUser)
                        viewModelEmotion.deleteEmotionMutableLiveData.observe(viewLifecycleOwner){status ->
                            if (status.equals("success")) {
                                val intent = Intent(activity!!, EmotionGaugeActivity::class.java)
                                startActivity(intent)
                                Animatoo.animateSlideLeft(activity!!)
                            }
                        }
                    }else{
                        val intent = Intent(activity!!, EmotionGaugeActivity::class.java)
                        startActivity(intent)
                        Animatoo.animateSlideLeft(activity!!)
                    }
                }
            }
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
            viewModelAnchoring.anchoringMutableLiveData.observe(viewLifecycleOwner) { anchoring ->
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
            viewModelAnchoring.databaseErrorAnchoring.observe(
                requireActivity()
            ) { error ->
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoals.setOnClickListener {
            viewModelValueGoals = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

            viewModelValueGoals.allGoals(uidUser!!)
            viewModelValueGoals.goalsMutableLiveData.observe(viewLifecycleOwner) { goals ->
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
                .setTitle(resources.getString(R.string.coming_soon))
                .setPositiveButton("Ok") { _, _ ->
                }
                .show()
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

    private fun showLoading(){
        binding.rvHorizontalReminder.visibility = View.GONE
        binding.pbReminderHome.visibility = View.VISIBLE
    }

    private fun goneLoading(){
        binding.rvHorizontalReminder.visibility = View.VISIBLE
        binding.pbReminderHome.visibility = View.GONE
    }

}