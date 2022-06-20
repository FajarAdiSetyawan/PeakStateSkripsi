package com.brainoptimax.peakmeup.ui.reminders.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentDetailReminderBinding
import com.brainoptimax.peakmeup.ui.MainActivity
import com.brainoptimax.peakmeup.ui.reminders.ReminderActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.bumptech.glide.Glide


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ID = "id"
private const val TITLE = "title"
private const val SUBTITLE = "subtitle"
private const val DESC = "desc"
private const val DATETIME = "datetime"
private const val IMG = "img"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailReminderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var idReminder: String? = null
    private var title: String? = null
    private var subtitle: String? = null
    private var desc: String? = null
    private var datetime: String? = null
    private var img: String? = null

    private var fragmentDetailReminderBinding: FragmentDetailReminderBinding? = null
    private val binding get() = fragmentDetailReminderBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idReminder = it.getString(ID)
            title = it.getString(TITLE)
            subtitle = it.getString(SUBTITLE)
            desc = it.getString(DESC)
            datetime = it.getString(DATETIME)
            img = it.getString(IMG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentDetailReminderBinding = FragmentDetailReminderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailReminderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, title: String, subtitle: String, desc: String, datetime: String, img: String) =
            DetailReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(TITLE, title)
                    putString(SUBTITLE, subtitle)
                    putString(DESC, desc)
                    putString(DATETIME, datetime)
                    putString(IMG, img)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        val activity = activity as AppCompatActivity?
        if (activity!!.supportActionBar != null) {
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        binding.tvDatetimeRemainder.text = datetime
        binding.tvDecsDetailRemainder.text = desc
        binding.tvSubtitleRemainder.text = subtitle
        binding.toolbar.title = title

        if (img!!.isEmpty()){
            when (title) {
                resources.getString(R.string.morning_routine) -> {
                    binding.imgBackdrop.setImageResource(R.drawable.ic_morning)
                }
                resources.getString(R.string.night_routine) -> {
                    binding.imgBackdrop.setImageResource(R.drawable.ic_night)
                }
                resources.getString(R.string.movement) -> {
                    binding.imgBackdrop.setImageResource(R.drawable.ic_movement)
                }
                resources.getString(R.string.fresh_air) -> {
                    binding.imgBackdrop.setImageResource(R.drawable.ic_fresh)
                }
                else -> {
                    binding.imgBackdrop.setImageResource(R.drawable.ic_placeholder_image)
                }
            }
        }else{
            Glide.with(requireActivity())
                .load(img)
                .into(binding.imgBackdrop)
        }

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, ReminderActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }


}