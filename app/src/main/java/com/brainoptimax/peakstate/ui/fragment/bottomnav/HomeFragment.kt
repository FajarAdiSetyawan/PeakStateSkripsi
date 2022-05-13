package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.HomeRemindersAdapter
import com.brainoptimax.peakstate.database.ReminderDatabaseHandler
import com.brainoptimax.peakstate.databinding.FragmentHomeBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.utils.Preferences
import com.brainoptimax.peakstate.viewmodel.bottomnav.HomeViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class HomeFragment : Fragment() {


    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var currentUserID: String? = null

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var databaseHandler: ReminderDatabaseHandler
    private lateinit var adapter: HomeRemindersAdapter
    private var reminderList = mutableListOf<Reminders>()

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

        preference = Preferences(requireActivity().applicationContext)
        binding.tvName.text = preference.getValues("username")

        val imgUrl = preference.getValues("imgUrl")
        if (imgUrl!!.isEmpty()){
            binding.ivAvatarHome.setImageResource(R.drawable.ic_profile)
        }else{
            Glide.with(this)
                .load(imgUrl)
                .into(binding.ivAvatarHome)
        }
    }




}