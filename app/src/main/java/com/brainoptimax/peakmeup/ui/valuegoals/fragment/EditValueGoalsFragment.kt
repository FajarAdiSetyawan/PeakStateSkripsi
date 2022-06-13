package com.brainoptimax.peakmeup.ui.valuegoals.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.DateFormat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainoptimax.peakmeup.adapter.valuegoals.EditGoalsAdapter
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.ui.valuegoals.ValueGoalsActivity
import com.brainoptimax.peakmeup.ui.valuegoals.fragment.bottomsheet.EditBottomSheetGoals
import com.brainoptimax.peakmeup.utils.Animatoo
import com.brainoptimax.peakmeup.viewmodel.valuegoals.ValueGoalsViewModel
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.databinding.FragmentEditValueGoalsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ID = "id"
private const val VALUE = "value"
private const val STATEMENT = "statement"
private const val DESC = "desc"
private const val IMG = "img"
private const val DATE = "date"
private const val TIME = "time"

/**
 * A simple [Fragment] subclass.
 * Use the [EditValueGoalsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditValueGoalsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: String? = null
    private var value: String? = null
    private var statement: String? = null
    private var desc: String? = null
    private var img: String? = null
    private var date: String? = null
    private var time: String? = null

    private var fragmentEditValueGoalsBinding: FragmentEditValueGoalsBinding? = null
    private val binding get() = fragmentEditValueGoalsBinding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseStorage: FirebaseStorage

    private lateinit var pathUri: Uri

    private var datePickerDialog: DatePickerDialog? = null
    private var dates: String? = null
    private lateinit var viewModel: ValueGoalsViewModel

    private lateinit var goals: MutableList<ToDo>

    private var editGoalsAdapter: EditGoalsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            value = it.getString(VALUE)
            statement = it.getString(STATEMENT)
            desc = it.getString(DESC)
            img = it.getString(IMG)
            date = it.getString(DATE)
            time = it.getString(TIME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEditValueGoalsBinding =
            FragmentEditValueGoalsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditValueGoalsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            id: String,
            value: String,
            statement: String,
            desc: String,
            img: String,
            date: String,
            time: String
        ) =
            EditValueGoalsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(VALUE, value)
                    putString(STATEMENT, statement)
                    putString(DESC, desc)
                    putString(IMG, img)
                    putString(DATE, date)
                    putString(TIME, time)
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(auth.currentUser!!.uid)
                .child("ValueGoals").child(id!!)
        // Initialize Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        viewModel = ViewModelProviders.of(this)[ValueGoalsViewModel::class.java]

        binding.tvValue.text = value
        binding.etSteatment.setText(statement)
        binding.tvDateGoals.text = date
        binding.tvTimeGoals.text = time
        binding.etDesc.setText(desc)

        if (img == "") {
            binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
        } else {
            Picasso.get().load(img).into(binding.ivIconValue)
        }

        binding.rvGoals.hasFixedSize()
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvGoals.layoutManager = linearLayoutManager
        editGoalsAdapter = EditGoalsAdapter(id)
        binding.rvGoals.adapter = editGoalsAdapter

        viewModel.allToDo(id!!)
        viewModel.todoMutableLiveData.observe(requireActivity()) { toDo ->
            editGoalsAdapter!!.setTodo(toDo)
            editGoalsAdapter!!.notifyDataSetChanged()
        }

        viewModel.databaseErrorToDo.observe(
            requireActivity()
        ) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.fabAddTodo.setOnClickListener {
            val args = Bundle()
            args.putString("key", id)
            val newFragment: BottomSheetDialogFragment = EditBottomSheetGoals()
            newFragment.arguments = args
            newFragment.show(requireActivity().supportFragmentManager, "TAG")
        }

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val date = calendar[Calendar.DATE]
        binding.cardDateGoals.setOnClickListener {
            datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, year, month, dayOfMonth ->
                    dates = getDateString(dayOfMonth, month, year)
                    val date = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                    binding.tvDateGoals.text = date
                }, year, month, date
            )
            datePickerDialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog!!.show()
        }

        binding.cardTimeGoals.setOnClickListener {
            showTimeDialog()
        }


        binding.cardValue.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity(), R.style.MaterialAlertDialogRounded)
            builder.setTitle("Value Goals")
            val item1 = arrayOf(
                resources.getString(R.string.community),
                resources.getString(R.string.education),
                resources.getString(R.string.finance),
                resources.getString(R.string.family),
                resources.getString(R.string.health)
            )
            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_reminder, null)
            builder.setView(customLayout)

            builder.setSingleChoiceItems(
                item1, -1
            ) { dialog, which ->
                binding.tvValue.text = item1[which]
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
                    binding.tvValue.text =
                        resources.getString(R.string.other)
                } else {
                    binding.tvValue.text = data
                }
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.md_blue_400)
            )
        }

        binding.ivEditImgValue.setOnClickListener {
            val openGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGallery, 1000)
        }

        binding.btnSave.setOnClickListener {

            val desc = binding.etDesc.text.toString().trim()
            val value = binding.tvValue.text.toString().trim()
            val statement = binding.etSteatment.text.toString().trim()
            val date = binding.tvDateGoals.text.toString().trim()
            val time = binding.tvTimeGoals.text.toString().trim()

            when {
                desc.isEmpty() -> {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.desc_goals_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                date.isEmpty() || date == resources.getString(R.string.date) -> {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.date_goals_empty),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                time.isEmpty() || time == resources.getString(R.string.time) -> {
                    Toast.makeText(
                        activity, resources.getString(R.string.time_goals_empty), Toast.LENGTH_SHORT
                    ).show()
                }
                statement.isEmpty() -> {
                    Toast.makeText(
                        activity, resources.getString(R.string.stat_empty), Toast.LENGTH_SHORT
                    ).show()
                }
                value.isEmpty() -> {
                    Toast.makeText(
                        activity, resources.getString(R.string.value_empty), Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                        .setTitle("Confirm the action")
                        .setMessage("Are you sure you update Value Goal ?")
                        .setPositiveButton("Ok") { _, _ ->
                            viewModel.updateGoal(
                                databaseReference,
                                view,
                                id!!,
                                value,
                                statement,
                                date,
                                time,
                                desc,
                            )
                            viewModel.status.observe(viewLifecycleOwner) { status ->
                                status?.let {
                                    //Reset status value at first to prevent multitriggering
                                    //and to be available to trigger action again
                                    viewModel.status.value = null

                                    Toast.makeText(requireActivity(), "Success Update", Toast.LENGTH_SHORT)
                                        .show()

                                    startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                                    Animatoo.animateSlideUp(requireContext())
                                }
                            }
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialog, which -> }
                        .show()


                }
            }
        }

        binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
                .setTitle(resources.getString(R.string.confirm_action))
                .setMessage(resources.getString(R.string.are_sure_delete) +" $value ?")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.deleteGoal(databaseReference, view, firebaseStorage, img!!)
                    viewModel.status.observe(viewLifecycleOwner) { status ->
                        status?.let {
                            //Reset status value at first to prevent multitriggering
                            //and to be available to trigger action again
                            viewModel.status.value = null
                            Toast.makeText(requireActivity(), resources.getString(R.string.success_delete) + " $value", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                            Animatoo.animateSlideUp(requireContext())
                        }
                    }
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which -> }
                .show()
        }

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        startActivity(Intent(context, ValueGoalsActivity::class.java)) // pindah ke login
                        Animatoo.animateSlideUp(requireContext())
                        return true
                    }
                }
                return false
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateString(year: Int, mMonth: Int, mDay: Int): String? {
        val calendar = Calendar.getInstance()
        calendar[year, mMonth] = mDay
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun showTimeDialog() {
        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        val calendar = Calendar.getInstance()

        /**
         * Initialize TimePicker Dialog
         */
        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { view, hourOfDay, minute ->
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */

                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */

                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */

                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                binding.tvTimeGoals.text = "$hourOfDay:$minute"
            },
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            /**
             * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
             */
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            /**
             * Cek apakah format waktu menggunakan 24-hour format
             */
            DateFormat.is24HourFormat(requireActivity())
        )
        timePickerDialog.show()
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
        viewModel.setImgValueGoal(
            databaseReference,
            storageReference,
            id!!,
            imgUri,
            view
        ).observe(this) { url ->
            if (url != null) {
                binding.ivIconValue.setImageURI(imgUri)
            } else {
                Snackbar.make(requireView(), "Upload Failed ", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentEditValueGoalsBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentEditValueGoalsBinding = null
    }
}