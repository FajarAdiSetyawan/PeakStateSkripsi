package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.HomeRemindersAdapter
import com.brainoptimax.peakstate.database.ReminderDatabaseHandler
import com.brainoptimax.peakstate.databinding.FragmentHomeBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.ui.activity.anchoring.AnchoringActivity
import com.brainoptimax.peakstate.ui.activity.breathing.IntroBreathingActivity
import com.brainoptimax.peakstate.ui.activity.emotion.EmotionGaugeActivity
import com.brainoptimax.peakstate.ui.activity.goals.ValueGoalsActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroAnchoringActivity
import com.brainoptimax.peakstate.ui.activity.intro.IntroEmotionActivity
import com.brainoptimax.peakstate.ui.activity.quiz.QuizActivity
import com.brainoptimax.peakstate.ui.activity.reminders.ListRemindersActivity
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import render.animations.Render
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat


class HomeFragment : Fragment() {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!

    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var currentUserID: String? = null

    private lateinit var databaseHandler: ReminderDatabaseHandler
    private lateinit var adapter: HomeRemindersAdapter
    private var reminderList = mutableListOf<Reminders>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = Render(activity!!)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        currentUserID = auth.currentUser?.uid

        databaseHandler = ReminderDatabaseHandler(activity!!)
        adapter = HomeRemindersAdapter()

        val refUser = databaseReference.child("Users").child(auth.currentUser!!.uid)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalReminder.layoutManager = layoutManager
        binding.rvHorizontalReminder.itemAnimator = DefaultItemAnimator()
        binding.rvHorizontalReminder.adapter = adapter

        binding.searchET.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterData(newText)
                return false
            }
        })

        showLoading()
        refUser.child("photoUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val photoUrl = snapshot.value.toString()
                        Picasso.get().load(photoUrl).into(binding.ivAvatarHome)
                        goneLoading()
                    } else {
                        val ref = storage.reference.child("users/$currentUserID/profile.jpg")
                        ref.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri?> { uri ->
                            goneLoading()
                            Picasso.get().load(uri).into(binding.ivAvatarHome)
                        }).addOnFailureListener {
                            goneLoading()
                            binding.ivAvatarHome.setImageResource(R.drawable.ic_baseline_account_circle_24)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        refUser.child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.value.toString()
                    binding.tvName.text = username
                }

                override fun onCancelled(error: DatabaseError) {}
            })


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
            val intent = Intent(activity!!, ListRemindersActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)
        }

        binding.tvMoreRemainder.setOnClickListener {
            val intent = Intent(activity!!, ListRemindersActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)

        }

        binding.llEmotion.setOnClickListener {
            val cutoff: Long = Date().time - TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS)

            val d = Date(cutoff)
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
            val intent = Intent(activity!!, IntroBreathingActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)
        }

        binding.llQuizzes.setOnClickListener {
            val intent = Intent(activity!!, QuizActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)
        }

        binding.btnAnchor.setOnClickListener {
            val intent = Intent(activity!!, IntroAnchoringActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)


        }

        binding.btnGoals.setOnClickListener {
            val intent = Intent(activity!!, ValueGoalsActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(activity!!)
        }
    }

    private fun showLoading() {
        binding.shimmerProfileHome.visibility = View.VISIBLE
        binding.shimmerProfileHome.startShimmer()

        binding.layoutProfileHome.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.shimmerProfileHome.visibility = View.INVISIBLE
        binding.shimmerProfileHome.stopShimmer()

        binding.layoutProfileHome.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        getAllRemindersFromDB()
    }

    // filter data
    private fun filterData(query: String) {
        val finalList =
            if (query.isEmpty()) reminderList else reminderList.filter {
                it.title.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault())) ||
                        it.description.lowercase(Locale.getDefault()).contains(
                            query.lowercase(
                                Locale.getDefault()
                            )
                        )
            }
        if (finalList.isNotEmpty()) {
            updateList(finalList.toMutableList())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(finalList: MutableList<Reminders>) {
        adapter.reminderList = finalList
        adapter.notifyDataSetChanged()

        if (reminderList.size > 0) {
            binding.rvHorizontalReminder.visibility = View.VISIBLE
            binding.layoutEmptyRemainderHome.visibility = View.GONE
        } else {
            binding.rvHorizontalReminder.visibility = View.INVISIBLE
            binding.layoutEmptyRemainderHome.visibility = View.VISIBLE
        }
    }

    private fun getAllRemindersFromDB() {
        reminderList = databaseHandler.getAll()
        updateList(reminderList)
    }


}