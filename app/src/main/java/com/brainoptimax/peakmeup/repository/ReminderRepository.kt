package com.brainoptimax.peakmeup.repository

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brainoptimax.peakmeup.model.Quiz
import com.brainoptimax.peakmeup.model.Reminder
import com.brainoptimax.peakmeup.model.valuegoals.ValueGoals
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class ReminderRepository(
    private val onRealtimeDbAddReminder: OnRealtimeDbAddReminder,
    private val onRealtimeDbAddImgReminder: OnRealtimeDbAddImgReminder,
    private val onRealtimeDbAllReminder: OnRealtimeDbAllReminder,
    private val onRealtimeDbUpdateReminder: OnRealtimeDbUpdateReminder,
    private val onRealtimeDbDeleteReminder: OnRealtimeDbDeleteReminder,
) {

    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Reminder")

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = FirebaseStorage.getInstance().reference

    fun addReminder(
        uidUser: String,
        title: String,
        subtitle: String,
        desc: String,
        date: String,
        time: String,
    ) {
        val add = databaseReference.child(uidUser)
        val idReminder = databaseReference.push().key
        val reminderAdd = Reminder(idReminder!!, title, subtitle, desc, date, time, "")
        add.child(idReminder).setValue(reminderAdd).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbAddReminder.onSuccessAddReminder("success")
            } else {
                onRealtimeDbAddReminder.onFailureAddReminder(it.exception!!.message.toString())
            }
        }
    }

    fun getAllReminder(uidUser: String){
        databaseReference.child(uidUser).get()
            .addOnCompleteListener { task ->
                val reminder: MutableList<Reminder> = ArrayList()

                if (task.isSuccessful) {
                    for (ds in task.result.children) {
                        val reminderList = ds.getValue(Reminder::class.java)
                        reminder.add(reminderList!!)
                    }
                    Log.d("TAG", "onDataChangeAllReminder: $reminder")
                    onRealtimeDbAllReminder.onSuccessAllReminder(reminder)
                } else {
                    Log.d("TAG", task.exception!!.message.toString())
                    onRealtimeDbAllReminder.onFailureAllReminder(task.exception!!.message.toString())
                }
            }
    }

    fun updateReminder(
        uidUser: String,
        idReminder:String,
        title: String,
        subtitle: String,
        desc: String,
        date: String,
        time: String,
    ) {
        val map: HashMap<String, Any> = HashMap()
        map["idReminder"] = idReminder
        map["title"] = title
        map["subtitle"] = subtitle
        map["description"] = desc
        map["date"] = date
        map["time"] = time
        databaseReference.child(uidUser).child(idReminder).updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbUpdateReminder.onSuccessUpdateReminder("success")
            } else {
                onRealtimeDbUpdateReminder.onFailureUpdateReminder(it.exception!!.message.toString())
            }
        }
    }

    fun deleteReminder(uidUser: String, idReminder: String, imgUrl: String) {
        if (imgUrl.isEmpty()) {
            databaseReference.child(uidUser).child(idReminder).removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onRealtimeDbDeleteReminder.onSuccessDeleteReminder("success")
                    } else {
                        onRealtimeDbDeleteReminder.onFailureDeleteReminder(it.exception!!.message.toString())
                    }
                }
        } else {
            firebaseStorage.getReferenceFromUrl(imgUrl).delete().addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    databaseReference.child(uidUser).child(idReminder).removeValue()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                onRealtimeDbDeleteReminder.onSuccessDeleteReminder("success")
                            } else {
                                onRealtimeDbDeleteReminder.onFailureDeleteReminder(it.exception!!.message.toString())
                            }
                        }
                }
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun setImgReminder(uidUser: String, idReminder: String, filePath: Uri): LiveData<String> {
        val signUpPhotoScreenResult = MutableLiveData<String>()
        val ref = storageReference.child("reminder/$uidUser/$idReminder/image.jpg")

        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    // Insert url data to user
                    databaseReference.child(uidUser).child(idReminder).child("imgurl")
                        .setValue(it.toString())
                    signUpPhotoScreenResult.postValue(it.toString())

                    onRealtimeDbAddImgReminder.onSuccessAddImgReminder("success")

                }
            }
            .addOnFailureListener {
                onRealtimeDbAddImgReminder.onFailureAddImgReminder(it.message.toString())
                signUpPhotoScreenResult.postValue(null)
            }

        return signUpPhotoScreenResult
    }

    interface OnRealtimeDbAddReminder {
        fun onSuccessAddReminder(status: String?)
        fun onFailureAddReminder(error: String?)
    }

    interface OnRealtimeDbAddImgReminder {
        fun onSuccessAddImgReminder(status: String?)
        fun onFailureAddImgReminder(error: String?)
    }


    interface OnRealtimeDbAllReminder {
        fun onSuccessAllReminder(reminder: List<Reminder>?)
        fun onFailureAllReminder(error: String?)
    }

    interface OnRealtimeDbUpdateReminder {
        fun onSuccessUpdateReminder(status: String?)
        fun onFailureUpdateReminder(error: String?)
    }

    interface OnRealtimeDbDeleteReminder {
        fun onSuccessDeleteReminder(status: String?)
        fun onFailureDeleteReminder(error: String?)
    }
}