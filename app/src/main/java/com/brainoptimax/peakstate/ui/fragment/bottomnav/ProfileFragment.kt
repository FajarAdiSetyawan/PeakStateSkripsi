package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentProfileBinding
import com.brainoptimax.peakstate.ui.fragment.PreferenceFragment
import com.brainoptimax.peakstate.utils.Preferences
import com.brainoptimax.peakstate.utils.PreferencesKey
import com.brainoptimax.peakstate.viewmodel.bottomnav.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*

class ProfileFragment : Fragment() {

    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = fragmentProfileBinding!!
    private lateinit var pathUri: Uri

    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var viewModel: ProfileViewModel

    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference

        // Initialize Shared Preferences
        preferences = Preferences(requireActivity())


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        binding.tvUsername.text = preferences.getValues("username")

        val imgUrl = preferences.getValues("imgUrl")!!
        if (imgUrl.isEmpty() || imgUrl == "" || imgUrl.equals("") || imgUrl.isBlank() || imgUrl == "blank"){
            binding.ivAvatarProfile.setImageResource(R.drawable.ic_profile)
        }else{
            Glide.with(this)
                .load(imgUrl)
                .into(binding.ivAvatarProfile)
        }

        binding.ivChangeAvatar.setOnClickListener {
            val openGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGallery, 1000)
        }

        // menampilkan setting dari class preferencefragment
        childFragmentManager.beginTransaction()
            .add(R.id.layout_setting, PreferenceFragment())
            .commit()
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
        viewModel.setSignUpPhotoScreen(
            databaseReference,
            storageReference,
            auth.currentUser!!.uid,
            imgUri,
            view
        ).observe(this) { url ->
            if (url != null) {
                preferences.setValues(PreferencesKey.IMGURL, url)
                binding.ivAvatarProfile.setImageURI(imgUri)
            } else {
                Snackbar.make(requireView(), "Upload Failed ", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentProfileBinding = null
    }


}