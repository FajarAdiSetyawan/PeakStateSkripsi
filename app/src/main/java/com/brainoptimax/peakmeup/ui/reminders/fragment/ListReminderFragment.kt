package com.brainoptimax.peakmeup.ui.reminders.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.adapter.reminder.ReminderAdapter
import com.brainoptimax.peakmeup.databinding.FragmentListReminderBinding
import com.brainoptimax.peakmeup.model.Reminder
import com.brainoptimax.peakmeup.ui.MainActivity
import com.brainoptimax.peakmeup.ui.reminders.ReminderActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
import java.util.*


class ListReminderFragment : Fragment() {

    private var fragmentListReminderBinding: FragmentListReminderBinding? = null
    private val binding get() = fragmentListReminderBinding!!

    private lateinit var preference: Preferences
    private lateinit var viewModel: ReminderViewModel
    private var reminderAdapter: ReminderAdapter? = null
    private var reminderList = mutableListOf<Reminder>()
    private lateinit var nav : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentListReminderBinding = FragmentListReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = Navigation.findNavController(requireView())

        viewModel = ViewModelProviders.of(this)[ReminderViewModel::class.java]

        preference = Preferences(requireActivity())
        val uidUser = preference.getValues("uid")

        binding.fabAddReminder.setOnClickListener {
            nav.navigate(R.id.action_listReminderFragment_to_addReminderFragment)
        }

        showLoading()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding.rvRemainder.layoutManager = layoutManager
        reminderAdapter = ReminderAdapter(requireActivity())
        binding.rvRemainder.adapter = reminderAdapter

        viewModel.allReminder(uidUser!!)
        viewModel.allReminderMutableLiveData.observe(requireActivity()){ reminder ->
            Log.d("TAG", "onDataChangeReminder: $reminder")
            goneLoading()
            if (reminder!!.isEmpty()){
                binding.rvRemainder.visibility = View.GONE
                binding.layoutEmptyRemainder.visibility = View.VISIBLE
            }else{
                binding.rvRemainder.visibility = View.VISIBLE
                binding.layoutEmptyRemainder.visibility = View.INVISIBLE
            }
            reminderAdapter!!.setReminder(reminder)
            reminderAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorAllReminder.observe(requireActivity()
        ) { error ->
            goneLoading()
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.btnBackMenu.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
            Animatoo.animateSlideUp(requireContext())
        }

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, MainActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }


    private fun showLoading() {
        binding.shimmerRemainderMore.startShimmer()
        binding.shimmerRemainderMore.visibility = View.VISIBLE
        binding.rvRemainder.visibility = View.INVISIBLE
    }

    private fun goneLoading() {
        binding.shimmerRemainderMore.stopShimmer()
        binding.shimmerRemainderMore.visibility = View.INVISIBLE
        binding.rvRemainder.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentListReminderBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentListReminderBinding = null
    }
}