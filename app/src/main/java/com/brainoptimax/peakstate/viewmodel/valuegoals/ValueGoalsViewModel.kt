package com.brainoptimax.peakstate.viewmodel.valuegoals

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakstate.R
import com.brainoptimax.peakstate.model.valuegoals.ToDo
import com.brainoptimax.peakstate.model.valuegoals.ValueGoals
import com.brainoptimax.peakstate.repository.ValueGoalsRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference


class ValueGoalsViewModel: ViewModel(), ValueGoalsRepository.OnRealtimeDbTaskComplete, ValueGoalsRepository.OnRealtimeDbTaskCompleteTodo {

    val goalsMutableLiveData = MutableLiveData<List<ValueGoals>?>()
    val databaseErrorGoals = MutableLiveData<String?>()

    val todoMutableLiveData = MutableLiveData<List<ToDo>?>()
    val databaseErrorToDo = MutableLiveData<DatabaseError?>()

    private lateinit var alertDialogLoading: AlertDialog


    private val valueGoalsRepository: ValueGoalsRepository = ValueGoalsRepository(this, this)

    var status = MutableLiveData<Boolean?>()

    val allData: Unit
        get() {
            valueGoalsRepository.getAllGoals()
        }

    fun allToDo(idGoals: String){
        valueGoalsRepository.getAllTodo(idGoals)
    }

    override fun onSuccess(valueGoals: List<ValueGoals>?) {
        goalsMutableLiveData.value = valueGoals
    }

    override fun onFailure(error: String?) {
        databaseErrorGoals.value = error
    }


    override fun onSuccessTodo(todo: List<ToDo>?) {
        todoMutableLiveData.value = todo
    }

    override fun onFailureTodo(error: DatabaseError?) {
        databaseErrorToDo.value = error!!
    }

    fun saveGoal(
        ref: DatabaseReference,
        view: View?,
        id: String,
        value: String,
        state: String,
        date: String,
        time: String,
        desc: String,
        img: String
    ) {
        val valueGoals = ValueGoals(
            id,
            value,
            state,
            time,
            date,
            desc,
            img
        )  // -> dengan data dari model Users
        ref.setValue(valueGoals).addOnCompleteListener {
            // jika berhasil disimpan
            if (it.isSuccessful) {
                status.value = true
            } else {
                // tampilkan dialog error
                val message: String =
                    it.exception!!.message.toString() // mengambil pesan error
                Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun updateGoal(
        ref: DatabaseReference,
        view: View?,
        id: String,
        value: String,
        state: String,
        date: String,
        time: String,
        desc: String
    ) {
        val map: HashMap<String, Any> = HashMap()
        map["id"] = id
        map["value"] = value
        map["statement"] = state
        map["date"] = date
        map["time"] = time
        map["descValue"] = desc
        ref.updateChildren(map).addOnCompleteListener {
            // jika berhasil disimpan
            if (it.isSuccessful) {
                status.value = true
            } else {
                // tampilkan dialog error
                val message: String =
                    it.exception!!.message.toString() // mengambil pesan error
                Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun addToDoList(
        refGoals: DatabaseReference,
        view: View?,
        id: String,
        goals: String,
        isCompleted: String,
    ){
        val toDo = ToDo(id, goals, isCompleted)
        refGoals.setValue(toDo).addOnCompleteListener {
            if (it.isSuccessful){
                status.value = true
            }else{
                val message: String =
                    it.exception!!.message.toString() // mengambil pesan error
                Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }


    @SuppressLint("NullSafeMutableLiveData")
    internal fun setImgValueGoal(
        mDatabase: DatabaseReference,
        mStorageRef: StorageReference,
        uid: String,
        filePath: Uri,
        view: View?
    ): LiveData<String> {
        val signUpPhotoScreenResult = MutableLiveData<String>()
        val ref = mStorageRef.child("value_goals/$uid/image.jpg")

        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    // Insert url data to user
                    closeLoadingDialog()
                    mDatabase.child("img").setValue(it.toString())
                    signUpPhotoScreenResult.postValue(it.toString())

                    Snackbar.make(view!!, "Image Uploaded", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            .addOnFailureListener {
                closeLoadingDialog()
                signUpPhotoScreenResult.postValue(null)

                Snackbar.make(view!!, it.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }

        return signUpPhotoScreenResult
    }


    fun openLoadingDialog(fragmentActivity: FragmentActivity) {

        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_loading, null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        alertDialogLoading = progress.create()
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        alertDialogLoading.show()
        val titleView = dialogView.findViewById<View>(R.id.tv_msg_dialog) as TextView
        titleView.text = fragmentActivity.resources.getString(R.string.msg_upload_pict)
        val lottie = dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.playAnimation()
        lottie.setAnimation(R.raw.upload)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }

}