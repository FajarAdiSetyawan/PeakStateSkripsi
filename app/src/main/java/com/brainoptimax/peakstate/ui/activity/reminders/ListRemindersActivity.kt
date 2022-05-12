package com.brainoptimax.peakstate.ui.activity.reminders

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.preference.PreferenceManager
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.RemindersAdapter
import com.brainoptimax.peakstate.database.ReminderDatabaseHandler
import com.brainoptimax.peakstate.databinding.ActivityListRemindersBinding
import com.brainoptimax.peakstate.model.Reminders
import com.brainoptimax.peakstate.services.AlarmReceiverReminder
import com.brainoptimax.peakstate.services.ReminderService
import com.brainoptimax.peakstate.utils.Animatoo
import com.brainoptimax.peakstate.utils.ConnectionType
import com.brainoptimax.peakstate.utils.NetworkMonitorUtil
import com.brainoptimax.peakstate.utils.ReminderUtils
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager


class ListRemindersActivity : AppCompatActivity(), RemindersAdapter.OnItemClickListener {

    private var activityListRemindersBinding: ActivityListRemindersBinding? = null
    private val binding get() = activityListRemindersBinding!!
    private val networkMonitor = NetworkMonitorUtil(this)

    private lateinit var databaseHandler: ReminderDatabaseHandler
    private lateinit var adapter: RemindersAdapter
    private var reminderList = mutableListOf<Reminders>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityListRemindersBinding = ActivityListRemindersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkConnection()
        loadTheme()

        databaseHandler = ReminderDatabaseHandler(this)
        adapter = RemindersAdapter(this)


        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.rvRemainder.layoutManager = llm
        binding.rvRemainder.adapter = adapter

        getAllRemindersFromDB()

        val from = intent.getStringExtra("from")
        if (from == "Notification") {
            val reminderId = intent.getLongExtra("reminderId", 0)
            val reminderById = databaseHandler.getReminderById(reminderId)
            showReminderAlert(reminderById)
        }

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
                filterData(newText)
                return false
            }
        })
    }


    // filter data
    private fun filterData(query: String) {
        val finalList = if (query.isEmpty()) reminderList else reminderList.filter {
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
            binding.rvRemainder.visibility = View.VISIBLE
            binding.layoutEmptyRemainder.visibility = View.GONE
        } else {
            binding.rvRemainder.visibility = View.GONE
            binding.layoutEmptyRemainder.visibility = View.VISIBLE
        }
    }

    private fun getAllRemindersFromDB() {
        reminderList = databaseHandler.getAll()
        updateList(reminderList)
    }


    private fun showReminderAlert(reminder: Reminders) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(reminder.title)
        builder.setMessage(reminder.description)
        builder.setPositiveButton("STOP ALARM") { dialog, _ ->
            ReminderUtils.showToastMessage(this, "Your alarm has been stopped")
            dialog.dismiss()
            stopAlarm()
            stopReminderService()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun stopAlarm() {
        val intent = Intent(this, AlarmReceiverReminder::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    private fun stopReminderService() {
        val reminderService = Intent(this, ReminderService::class.java)
        stopService(reminderService)
    }

    override fun onItemClick(
        reminder: Reminders,
        position: Int
    ) {
        startActivity(
            Intent(this, AddRemindersActivity::class.java)
                .putExtra("reminder", reminder)
        )
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

    // fungsi untuk mengecek internet secara berulang
    override fun onResume() {
        super.onResume()
        getAllRemindersFromDB()
        networkMonitor.register()
    }

    // fungsi untuk menghentikan pengecekan internet
    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    // Check Internet Connection
    private fun checkConnection() {
        // buat dialog error internet
        val li = LayoutInflater.from(this)
        // panggil layout dialog error
        val promptsView: View = li.inflate(R.layout.dialog_error, null)
        val alertDialogBuilder = android.app.AlertDialog.Builder(
            this
        )
        alertDialogBuilder.setView(promptsView)
        // dialog error tidak dapat di tutup selain menekan tombol close
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20)

        // panggil textview judul error dari layout dialog
        val titleView = promptsView.findViewById<View>(R.id.tv_title_dialog_error) as TextView
        titleView.text = (resources.getString(R.string.offline))

        // panggil textview desc error dari layout dialog
        val descView = promptsView.findViewById<View>(R.id.tv_desc_dialog_error) as TextView
        descView.text = (resources.getString(R.string.msgoffline))

        // panggil animasi lottie error dari layout dialog
        val lottieView =
            promptsView.findViewById<View>(R.id.lottie_dialog_error) as LottieAnimationView
        lottieView.playAnimation()
        lottieView.setAnimation(R.raw.offline)

        // panggil button error dari layout dialog
        val btnView = promptsView.findViewById<View>(R.id.btn_error_dialog) as Button
        btnView.text = (resources.getString(R.string.connect))
        btnView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            this.let { it1 -> Animatoo.animateSlideUp(it1) }
        }
        // panggil gambar close error dari layout dialog
        val ivDismiss = promptsView.findViewById<View>(R.id.iv_dismiss_error_dialog) as ImageView
        ivDismiss.setOnClickListener {
            networkMonitor.result = { isAvailable, type ->
                runOnUiThread {
                    // cek ada status wifi/data seluler
                    when (isAvailable) {
                        true -> {
                            when (type) {
                                ConnectionType.Wifi -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                    // dialog error ditutup
                                    alertDialog.dismiss()
                                }
                                ConnectionType.Cellular -> {
                                    Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                    alertDialog.dismiss()
                                }
                                else -> {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                        false -> {
                            Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                            // dialog error ditampilkan
                            alertDialog.show()
                        }
                    }
                }
            }
        }
        alertDialog.window!!.setBackgroundDrawable(inset)
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Wifi Connection")
                                alertDialog.dismiss()
                            }
                            ConnectionType.Cellular -> {
                                Log.i("NETWORK_MONITOR_STATUS", "Cellular Connection")
                                alertDialog.dismiss()
                            }
                            else -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        alertDialog.show()
                    }
                }
            }
        }
    }

}