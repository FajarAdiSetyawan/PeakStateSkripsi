package com.brainoptimax.peakmeup.ui.reminders

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.brainoptimax.peakmeup.adapter.reminder.RemindersAdapter
import com.brainoptimax.peakmeup.model.Reminders
import com.brainoptimax.peakmeup.utils.Animatoo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainoptimax.peakmeup.adapter.reminder.ReminderClickListener
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.ActivityListRemindersBinding


class ListRemindersActivity : AppCompatActivity() {

    private var activityListRemindersBinding: ActivityListRemindersBinding? = null
    private val binding get() = activityListRemindersBinding!!

    private lateinit var adapter: RemindersAdapter
    private lateinit var viewModel: ReminderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityListRemindersBinding = ActivityListRemindersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadTheme()

        viewModel = ViewModelProviders.of(this)[ReminderViewModel::class.java]
        updateRecyclerView()
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvRemainder)

        // pindah ke add reminder
        binding.fabAddReminder.setOnClickListener {
            startActivity(Intent(this, AddRemindersActivity::class.java))
            Animatoo.animateSwipeLeft(this)
            finish()
        }

        // kembali ke menu
        binding.btnBackMenu.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
        }


        binding.searchET.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private val clickListener: ReminderClickListener = object : ReminderClickListener {
        override fun click(reminders: Reminders?) {
            val intent = Intent(this@ListRemindersActivity, DetailReminderActivity::class.java)
            intent.putExtra(DetailReminderActivity.EXTRA_TITLE, reminders!!.title)
            intent.putExtra(DetailReminderActivity.EXTRA_DESC, reminders.description)
            intent.putExtra(DetailReminderActivity.EXTRA_SUBTITLE, reminders.subtitle)
            intent.putExtra(DetailReminderActivity.EXTRA_DATE, reminders.date)
            intent.putExtra(DetailReminderActivity.EXTRA_TIME, reminders.time)
            startActivity(intent)
        }

        override fun OnLongClick(reminders: Reminders?) {
            val intent = Intent(this@ListRemindersActivity, AddRemindersActivity::class.java)
            intent.putExtra("updateReminders", reminders)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView() {
        viewModel.getAllReminders!!.observe(this) { reminderEntities: List<Reminders?>? ->
            binding.rvRemainder.setHasFixedSize(true)
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            binding.rvRemainder.layoutManager = llm

            if (reminderEntities!!.isNotEmpty()) {
                binding.rvRemainder.visibility = View.VISIBLE
                binding.layoutEmptyRemainder.visibility = View.GONE
            } else {
                binding.rvRemainder.visibility = View.GONE
                binding.layoutEmptyRemainder.visibility = View.VISIBLE
            }

            adapter = RemindersAdapter(reminderEntities as MutableList<Reminders>, this, clickListener)
            binding.rvRemainder.adapter = adapter
            adapter.notifyDataSetChanged()
        }



    }


    private var simpleCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteReminders(adapter.getReminderAt(viewHolder.adapterPosition))
                Toast.makeText(this@ListRemindersActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
        }

    // methode kembali
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }

    private fun loadTheme() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val theme = preference.getString(
            getString(R.string.pref_key_theme),
            getString(R.string.pref_theme_entry_auto)
        )
        when {
            theme.equals(getString(R.string.pref_theme_entry_auto)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            theme.equals(getString(R.string.pref_theme_entry_dark)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            theme.equals(getString(R.string.pref_theme_entry_light)) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun updateTheme(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        return true
    }


}