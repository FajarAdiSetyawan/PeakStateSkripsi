package com.brainoptimax.peakstate.ui.activity.goals

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
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.adapter.valuegoals.DetailValueGoalsAdapter
import com.brainoptimax.peakstate.databinding.ActivityDetailValueGoalsBinding
import com.brainoptimax.peakstate.model.Goals
import com.brainoptimax.peakstate.utils.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
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

class DetailValueGoalsActivity : AppCompatActivity() {

    private var activityDetailValueGoalsBinding: ActivityDetailValueGoalsBinding? = null
    private val binding get() = activityDetailValueGoalsBinding!!

    // memanggil firebase auth (user yg login)
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var goals: ArrayList<Goals>
    private lateinit var pathUri: Uri

    var currentUserID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailValueGoalsBinding = ActivityDetailValueGoalsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvGoals.layoutManager = layoutManager

        val intent = intent
        val valueID = intent.getStringExtra("valueId")

        currentUserID = auth.currentUser?.uid

        binding.tvValueid.text = valueID

        showLoading()
//        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
//            .child(valueID!!).child("imgValue")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.equals("null")){
//
//                    }
////                    if (snapshot.exists()) {
////
////                    } else {
////                        val ref = storage.reference.child("value/$currentUserID/$valueID/value.jpg")
////                        ref.downloadUrl.addOnSuccessListener { uri ->
////                            goneLoading()
////                            Picasso.get().load(uri).into(binding.ivIconValue)
////                        }.addOnFailureListener {
////
////                        }
////                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })


        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
            .child(valueID!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val value = dataSnapshot.child("value").value.toString()
                        val statement = dataSnapshot.child("statement").value.toString()
                        val descValue = dataSnapshot.child("descValue").value.toString()
                        val datetime = dataSnapshot.child("dateTime").value.toString()
                        val imgSrc = dataSnapshot.child("imgValue").value.toString()

                        binding.tvValue.text = value
                        binding.tvStatement.text = statement
                        binding.tvDesc.text = descValue
                        binding.tvDatetime.text = datetime

                        if (imgSrc == "null"){
                            goneLoading()
                            binding.ivIconValue.setImageResource(R.drawable.ic_family_placeholder)
                        }else{
                            goneLoading()
                            Picasso.get().load(imgSrc).into(binding.ivIconValue)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DetailValueGoalsActivity,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
            .child(valueID).child("goals")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        goals = arrayListOf()

                        for (item in dataSnapshot.children) {
                            val goalsList = item.getValue(Goals::class.java)
                            this@DetailValueGoalsActivity.goals.add(goalsList!!)
                        }
                        binding.rvGoals.adapter = DetailValueGoalsAdapter(goals, valueID)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DetailValueGoalsActivity,
                        "Error " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        binding.ivEditImgValue.setOnClickListener {
            val openGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGallery, 1000)
        }

        binding.backMain.setOnClickListener {
            super.onBackPressed()
            Animatoo.animateSwipeLeft(this)
            finish()
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

    fun compressAndSetImage(result: Uri) {
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + job)
        val fileUri = getFilePathFromUri(result, this)
        uiScope.launch {
            val compressedImageFile = Compressor.compress(this@DetailValueGoalsActivity, File(fileUri!!.path)) {
                quality(50) // combine with compressor constraint
                format(Bitmap.CompressFormat.JPEG)
            }
            pathUri = Uri.fromFile(compressedImageFile)

            this@DetailValueGoalsActivity.runOnUiThread {
                uploadImageToFirebase(pathUri)
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
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
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
        textUpload.text = (resources.getString(R.string.msg_upload_pict_value))
        alertDialogLoading.show()
        showLoading()
        val valueID = binding.tvValueid.text.toString()
        val ref = storage.reference.child("value/$currentUserID/$valueID/value.jpg")
        ref.putFile(imgUri).addOnSuccessListener { taskSnapshot ->
            if (taskSnapshot.metadata != null) {
                if (taskSnapshot.metadata!!.reference != null) {
                    val result = taskSnapshot.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        databaseReference.child("Users").child(auth.currentUser!!.uid).child("ValueGoals")
                            .child(valueID).child("imgValue").setValue(imageUrl)
                            .addOnSuccessListener {
                                goneLoading()
                                Picasso.get().load(uri).into(binding.ivIconValue)
                                Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                                alertDialogLoading.dismiss()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed Upload", Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed Upload", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed Upload", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSwipeLeft(this)
        finish()
    }

    fun showLoading(){
        binding.shimmerImageValue.visibility = View.VISIBLE
        binding.shimmerImageValue.startShimmer()
        binding.shape.visibility = View.GONE
    }

    fun goneLoading(){
        binding.shimmerImageValue.visibility = View.GONE
        binding.shimmerImageValue.stopShimmer()
        binding.shape.visibility = View.VISIBLE
    }
}