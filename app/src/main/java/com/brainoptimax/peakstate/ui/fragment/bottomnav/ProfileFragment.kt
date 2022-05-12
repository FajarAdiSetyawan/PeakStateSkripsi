package com.brainoptimax.peakstate.ui.fragment.bottomnav

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.databinding.FragmentProfileBinding
import com.brainoptimax.peakstate.ui.fragment.PreferenceFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
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
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var currentUserID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        currentUserID = auth.currentUser?.uid

        showLoading()
        databaseReference.child("Users").child(auth.currentUser!!.uid).child("photoUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        goneLoading()
                        val photoUrl = snapshot.value.toString()
                        Picasso.get().load(photoUrl).into(binding.ivAvatarProfile)
                    } else {
                        val ref = storage.reference.child("users/$currentUserID/profile.jpg")
                        ref.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri?> { uri ->
                            goneLoading()
                            Picasso.get().load(uri).into(binding.ivAvatarProfile)
                        }).addOnFailureListener {
                            goneLoading()
                            binding.ivAvatarProfile.setImageResource(R.drawable.ic_baseline_account_circle_24)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        databaseReference.child("Users").child(currentUserID!!).child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.value.toString()
                    binding.tvUsername.text = username
                }

                override fun onCancelled(error: DatabaseError) {}
            })

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

    fun compressAndSetImage(result: Uri) {
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        val fileUri = getFilePathFromUri(result, context!!)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(context!!, File(fileUri!!.path)) {
                quality(50) // combine with compressor constraint
                format(Bitmap.CompressFormat.JPEG)
            }
            pathUri = Uri.fromFile(compressedImageFile)

            activity!!.runOnUiThread {
                pathUri.let {
                    //set image here
                    uploadImageToFirebase(pathUri)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun getFilePathFromUri(uri: Uri?, context: Context?): Uri? {
        val fileName: String? = getFileName(uri, context)
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

    fun getFileName(uri: Uri?, context: Context?): String? {
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

    fun getFileExtension(uri: Uri?, context: Context?): String? {
        val fileType: String? = context?.contentResolver?.getType(uri!!)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    fun getFileNameFromCursor(uri: Uri?, context: Context?): String? {
        val fileCursor: Cursor? = context?.contentResolver
            ?.query(uri!!, arrayOf<String>(OpenableColumns.DISPLAY_NAME), null, null, null)
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
        // tampilkan dialog loading register
        val li = LayoutInflater.from(activity)
        val promptsView: View = li.inflate(R.layout.dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        val alertDialogLoading = alertDialogBuilder.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        val lottie =
            promptsView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.playAnimation()
        lottie.setAnimation(R.raw.upload)
        val textUpload = promptsView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        textUpload.text = (resources.getString(R.string.msg_upload_pict))
        alertDialogLoading.show()
        showLoading()
        val ref = storage.reference.child("users/$currentUserID/profile.jpg")
        ref.putFile(imgUri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                if (taskSnapshot.metadata != null) {
                    if (taskSnapshot.metadata!!.reference != null) {
                        val result = taskSnapshot.storage.downloadUrl
                        result.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            databaseReference.child("Users").child(currentUserID!!)
                                .child("photoUrl").setValue(imageUrl)
                                .addOnSuccessListener {
                                    Picasso.get().load(uri).into(binding.ivAvatarProfile)
                                    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT)
                                        .show()
                                    goneLoading()
                                    alertDialogLoading.dismiss()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed Upload", Toast.LENGTH_SHORT).show()
                                }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed Upload", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }).addOnFailureListener(OnFailureListener {
                Toast.makeText(context, "Failed Upload", Toast.LENGTH_SHORT).show()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentProfileBinding = null
    }

    fun goneLoading() {
        binding.layoutProfile.visibility = View.VISIBLE
        binding.shimmerImageProfile.stopShimmer()
        binding.shimmerImageProfile.visibility = View.INVISIBLE
    }

    fun showLoading() {
        binding.layoutProfile.visibility = View.INVISIBLE
        binding.shimmerImageProfile.startShimmer()
        binding.shimmerImageProfile.visibility = View.VISIBLE
    }

}