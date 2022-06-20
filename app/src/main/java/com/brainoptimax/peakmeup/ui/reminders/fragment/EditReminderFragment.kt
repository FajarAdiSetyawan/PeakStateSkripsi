package com.brainoptimax.peakmeup.ui.reminders.fragment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentEditReminderBinding
import com.brainoptimax.peakmeup.services.AlarmReceiverReminder
import com.brainoptimax.peakmeup.ui.reminders.ReminderActivity
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.utils.Preferences
import com.brainoptimax.peakmeup.utils.ReminderUtils
import com.brainoptimax.peakmeup.viewmodel.reminder.ReminderViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.util.*

private const val ID = "id"
private const val TITLE = "title"
private const val SUBTITLE = "subtitle"
private const val DESC = "desc"
private const val DATE = "date"
private const val TIME = "time"
private const val IMG = "img"

class EditReminderFragment : Fragment() {

    private var idReminder: String? = null
    private var title: String? = null
    private var subtitle: String? = null
    private var desc: String? = null
    private var date: String? = null
    private var time: String? = null
    private var img: String? = null

    private var fragmentEditReminderBinding: FragmentEditReminderBinding? = null
    private val binding get() = fragmentEditReminderBinding!!

    private lateinit var preference: Preferences
    private lateinit var viewModel: ReminderViewModel

    private var calendar = Calendar.getInstance()
    private lateinit var nav : NavController
    private var uidUser = ""

    private lateinit var pathUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idReminder = it.getString(ID)
            title = it.getString(TITLE)
            subtitle = it.getString(SUBTITLE)
            desc = it.getString(DESC)
            date = it.getString(DATE)
            time = it.getString(TIME)
            img = it.getString(IMG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEditReminderBinding = FragmentEditReminderBinding.inflate(layoutInflater, container, false)
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
        fun newInstance(id: String, title: String, subtitle: String, desc: String, date: String, time: String, img: String) =
            EditReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(TITLE, title)
                    putString(SUBTITLE, subtitle)
                    putString(DESC, desc)
                    putString(DATE, date)
                    putString(TIME, time)
                    putString(IMG, img)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[ReminderViewModel::class.java]

        nav = Navigation.findNavController(requireView())

        preference = Preferences(requireActivity())
        uidUser = preference.getValues("uid")!!

        binding.tvDateReminder.text = date
        binding.tvTimeReminder.text = time
        binding.etNote.setText(desc)
        binding.tvSubtitleRemainder.text = subtitle
        binding.tvTitleRemainder.text = title

        createNotificationChannel()

        if (img!!.isEmpty()){
            when (title) {
                resources.getString(R.string.morning_routine) -> {
                    binding.tvTitleRemainder.setTextColor(Color.parseColor("#F4D35E"))
                    binding.ivIconReminder.setImageResource(R.drawable.ic_morning)
                }
                resources.getString(R.string.night_routine) -> {
                    binding.tvTitleRemainder.setTextColor(Color.parseColor("#19647E"))
                    binding.ivIconReminder.setImageResource(R.drawable.ic_night)
                }
                resources.getString(R.string.movement) -> {
                    binding.tvTitleRemainder.setTextColor(Color.parseColor("#EE964B"))
                    binding.ivIconReminder.setImageResource(R.drawable.ic_movement)
                }
                resources.getString(R.string.fresh_air) -> {
                    binding.tvTitleRemainder.setTextColor(Color.parseColor("#28AFB0"))
                    binding.ivIconReminder.setImageResource(R.drawable.ic_fresh)
                }
                else -> {
                    binding.tvTitleRemainder.setTextColor(Color.parseColor("#84AEFF"))
                    binding.ivIconReminder.setImageResource(R.drawable.ic_placeholder_image)
                }
            }
        }else{
            Glide.with(requireActivity())
                .load(img)
                .into(binding.ivIconReminder)
        }

        binding.cardTitle.setOnClickListener {
            val item1 = arrayOf(
                resources.getString(R.string.morning_routine),
                resources.getString(R.string.night_routine),
                resources.getString(R.string.movement),
                resources.getString(R.string.fresh_air)
            )

            val builder = AlertDialog.Builder(requireActivity(), R.style.MaterialAlertDialogRounded)
            builder.setTitle(resources.getString(R.string.title_reminder))
            // set the custom layout
            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_reminder, null)
            builder.setView(customLayout)

            builder.setSingleChoiceItems(
                item1, -1
            ) { dialog, which ->
                binding.tvTitleRemainder.text = item1[which]
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                editText.setText(item1[which])
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val editText: TextInputEditText = customLayout.findViewById(R.id.it_title_remainder)
                val data = editText.text.toString()
                if (data.isEmpty()) {
                    binding.tvTitleRemainder.text =
                        resources.getString(R.string.title_reminder)
                } else {
                    binding.tvTitleRemainder.text = data
                }
            }
            builder.setNegativeButton(
                resources.getString(R.string.cancel)
            ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.md_blue_400)
            )


        }

        binding.cardSubtitle.setOnClickListener {
            val item2 = arrayOf(
                resources.getString(R.string.write_journal),
                resources.getString(R.string.anchoring),
                resources.getString(R.string.visualization),
                resources.getString(R.string.breathing_exercise),
                resources.getString(R.string.brainwave_entrainment),
                resources.getString(R.string.values_and_goals),
            )

            val builder = AlertDialog.Builder(requireActivity(), R.style.MaterialAlertDialogRounded)
            builder.setTitle(resources.getString(R.string.sub_title_reminder))
            // set the custom layout
            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_reminder, null)
            builder.setView(customLayout)

            builder.setSingleChoiceItems(
                item2, -1
            ) { _, which ->
                binding.tvSubtitleRemainder.text = item2[which]
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                //                        String data = editText.getText().toString();
                //                        tvTitle.setText(data);
                editText.setText(item2[which])
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val editText: TextInputEditText =
                    customLayout.findViewById(R.id.it_title_remainder)
                val data = editText.text.toString()
                if (data.isEmpty()) {
                    binding.tvSubtitleRemainder.text =
                        resources.getString(R.string.sub_title_reminder)
                } else {
                    binding.tvSubtitleRemainder.text = data
                }
            }
            builder.setNegativeButton(
                resources.getString(R.string.cancel)
            ) { dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.md_blue_400)
            )

        }

        binding.cardTimeReminder.setOnClickListener {
            setTime()
        }

        binding.cardDateReminder.setOnClickListener {
            setDate()
        }

        binding.btnSaveRemainder.setOnClickListener {
            val getTime: String = binding.tvTimeReminder.text.toString().trim()
            val getDate: String = binding.tvDateReminder.text.toString().trim()
            val getTitle: String =
                binding.tvTitleRemainder.text.toString().trim()
            val getSubtitle: String =
                binding.tvSubtitleRemainder.text.toString().trim()
            val getNote: String = binding.etNote.text.toString().trim()

            if (getTitle.isEmpty() || getTitle == resources.getString(R.string.title_reminder)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.title_blank), Toast.LENGTH_SHORT).show()
            } else if (getSubtitle.isEmpty() || getSubtitle == resources.getString(R.string.sub_title_reminder)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.sub_title_blank), Toast.LENGTH_SHORT).show()
            } else if (getDate.isEmpty()) {
                Toast.makeText(requireActivity(), resources.getString(R.string.date_blank), Toast.LENGTH_SHORT).show()
            } else if (getTime.isEmpty() || getTime == resources.getString(R.string.time)) {
                Toast.makeText(requireActivity(), resources.getString(R.string.blank), Toast.LENGTH_SHORT).show()
            } else if (getNote.isEmpty()) {
                Toast.makeText(requireActivity(), resources.getString(R.string.note_blank), Toast.LENGTH_SHORT).show()
                binding.etNote.error = resources.getString(R.string.note_blank)
                binding.etNote.requestFocus()
            } else {
                binding.etNote.error = null

                MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                    .setTitle(resources.getString(R.string.confirm_action))
                    .setMessage(resources.getString(R.string.dialog_reminder_update_msg))
                    .setPositiveButton("Ok") { _, _ ->
                        viewModel.openLoadingSaveDialog(requireActivity())
                        viewModel.updateReminder(uidUser!!, idReminder!!, getTitle, getSubtitle, getNote, getDate, getTime)
                        viewModel.updateReminderMutableLiveData.observe(viewLifecycleOwner){ status ->
                            if (status.equals("success")){
                                setNotification(getTitle, getSubtitle, getNote)
                                startActivity(Intent(context, ReminderActivity::class.java)) // pindah ke login
                                Animatoo.animateSlideUp(requireContext())
                                viewModel.closeLoadingDialog()
                            }
                        }

                        viewModel.databaseErrorUpdateReminder.observe(viewLifecycleOwner
                        ) { error ->
                            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton(
                        resources.getString(R.string.cancel)
                    ) { dialog, which -> }
                    .show()
            }
        }

        binding.btnDeleteRemainder.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) + title + " ?")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.deleteReminder(uidUser!!, idReminder!!, img!!)
                    viewModel.deleteReminderMutableLiveData.observe(viewLifecycleOwner){ status ->
                        if (status.equals("success")){
                            Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " " + title, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, ReminderActivity::class.java)) // pindah ke login
                            Animatoo.animateSlideUp(requireContext())
                        }
                    }

                    viewModel.databaseErrorDeleteReminder.observe(viewLifecycleOwner
                    ) { error ->
                        Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(
                    resources.getString(R.string.cancel)
                ) { dialog, which -> }
                .show()
        }

        binding.ivEditImgReminder.setOnClickListener {
            val openGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGallery, 1000)
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

    private fun setDate() {
        val Year = calendar[Calendar.YEAR]
        val Month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        val datePickerDialog = DatePickerDialog(requireActivity(), { view, YEAR, MONTH, DATE ->
            calendar[Calendar.YEAR] = YEAR
            calendar[Calendar.MONTH] = MONTH
            calendar[Calendar.DATE] = DATE
        }, Year, Month, date)
        datePickerDialog.show()
        updateDate()
    }

    private fun updateDate() {
        val formattedDate =
            ReminderUtils.getFormattedDateInString(calendar.timeInMillis, "dd-MMM-yyyy")
        binding.tvDateReminder.text = formattedDate
    }

    private fun setTime() {
        val Hour = calendar[Calendar.HOUR_OF_DAY]
        val Minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(requireActivity(), { view, hour, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            updateTime(hour, minute)
        }, Hour, Minute, true)
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        binding.tvTimeReminder.text = "$hour:$minute"
    }

    @SuppressLint("MissingPermission")
    private fun setNotification(title: String, subTitle: String, desc: String) {
        val intent = Intent(requireActivity(), AlarmReceiverReminder::class.java)
        intent.action = "com.brainoptimax.peakstate.reminders"
        intent.putExtra("ReminderTitle", title)
        intent.putExtra("ReminderSubTitle", subTitle)
        intent.putExtra("ReminderDesc", desc)

        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 101, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "reminder_notify"
            val description = "To Notify Reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_notify", name, importance)
            channel.description = description
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val imgUri = data!!.data

                compressAndSetImage(imgUri!!)
            }
        }
    }

    private fun compressAndSetImage(result: Uri) {
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        val fileUri = getFilePathFromUri(result, requireContext())
        uiScope.launch {
            val compressedImageFile = Compressor.compress(requireContext(), File(fileUri!!.path!!)) {
                quality(50) // combine with compressor constraint
                format(Bitmap.CompressFormat.JPEG)
            }
            pathUri = Uri.fromFile(compressedImageFile)

            requireActivity().runOnUiThread {
                uploadImageToFirebase(pathUri)
            }
        }
    }

    @Throws(IOException::class)
    fun getFilePathFromUri(uri: Uri?, context: Context?): Uri? {
        val fileName: String = getFileName(uri, context)
        val file = File(context?.externalCacheDir, fileName)
        file.createNewFile()
        FileOutputStream(file).use { outputStream ->
            context?.contentResolver?.openInputStream(uri!!).use { inputStream ->
                copyFile(inputStream, outputStream)
                outputStream.flush()
            }
        }
        return Uri.fromFile(file)
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream?, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int? = null
        while (`in`?.read(buffer).also { read = it!! } != -1) {
            read?.let { out.write(buffer, 0, it) }
        }
    }//copyFile ends

    private fun getFileName(uri: Uri?, context: Context?): String {
        var fileName: String? = getFileNameFromCursor(uri, context)
        if (fileName == null) {
            val fileExtension: String? = getFileExtension(uri, context)
            fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
        } else if (!fileName.contains(".")) {
            val fileExtension: String? = getFileExtension(uri, context)
            fileName = "$fileName.$fileExtension"
        }
        return fileName
    }

    private fun getFileExtension(uri: Uri?, context: Context?): String? {
        val fileType: String? = context?.contentResolver?.getType(uri!!)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @SuppressLint("Recycle")
    private fun getFileNameFromCursor(uri: Uri?, context: Context?): String? {
        val fileCursor: Cursor? = context?.contentResolver
            ?.query(uri!!, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        var fileName: String? = null
        if (fileCursor != null && fileCursor.moveToFirst()) {
            val cIndex: Int = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cIndex != -1) {
                fileName = fileCursor.getString(cIndex)
            }
        }
        return fileName
    }

    private fun uploadImageToFirebase(imgUri: Uri) {

        viewModel.openLoadingDialog(requireActivity())
        viewModel.setImageReminder(uidUser, idReminder!!, imgUri)

        viewModel.imageReminderMutableLiveData.observe(requireActivity()) { success ->
            viewModel.closeLoadingDialog()
            if (success.equals("success")){
                binding.ivIconReminder.setImageURI(imgUri)
            }
        }
        viewModel.databaseErrorImageReminder.observe(requireActivity()) { error ->
            viewModel.closeLoadingDialog()
            Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEditReminderBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEditReminderBinding = null
    }
}