package com.brainoptimax.peakmeup.repository

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.model.valuegoals.ValueGoals
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ValueGoalsRepository(
    private val onRealtimeDbAddValueGoals: OnRealtimeDbAddValueGoals,
    private val onRealtimeDbAllValueGoals: OnRealtimeDbAllValueGoals,
    private val onRealtimeDbUpdateGoals: OnRealtimeDbUpdateGoals,
    private val onRealtimeDbImageGoals: OnRealtimeDbImageGoals,
    private val onRealtimeDbDeleteGoals: OnRealtimeDbDeleteGoals,
    private val onRealtimeDbTodo: OnRealtimeDbTodo,
    private val onRealtimeDbDoneTodo: OnRealtimeDbDoneTodo,
    private val onRealtimeDbNotDoneTodo: OnRealtimeDbNotDoneTodo,
    private val onRealtimeDbAddTodo: OnRealtimeDbAddTodo,
    private val onRealtimeDbDeleteTodo: OnRealtimeDbDeleteTodo,
) {
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("ValueGoals")

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference: StorageReference = FirebaseStorage.getInstance().reference

    fun addValueGoals(
        uidUser: String,
        value: String,
        statement: String,
        date: String,
        time: String,
        desc: String
    ) {
        val add = databaseReference.child(uidUser)
        val idGoals = databaseReference.push().key
        val goalsAdd = ValueGoals(idGoals, value, statement, time, date, desc, "")
        add.child(idGoals!!).setValue(goalsAdd).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbAddValueGoals.onSuccessAddValueGoals(idGoals)
            } else {
                onRealtimeDbAddValueGoals.onFailureAddValueGoals(it.exception!!.message.toString())
            }
        }
    }

    fun updateValueGoals(
        uidUser: String,
        idGoals: String,
        value: String,
        statement: String,
        date: String,
        time: String,
        desc: String
    ) {
        val map: HashMap<String, Any> = HashMap()
        map["idGoals"] = idGoals
        map["value"] = value
        map["statement"] = statement
        map["date"] = date
        map["time"] = time
        map["descValue"] = desc
        databaseReference.child(uidUser).child(idGoals).updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbUpdateGoals.onSuccessUpdateGoals("success")
            } else {
                onRealtimeDbUpdateGoals.onFailureUpdateGoals(it.exception!!.message.toString())
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun setImgValueGoal(uidUser: String, idGoals: String, filePath: Uri): LiveData<String> {
        val signUpPhotoScreenResult = MutableLiveData<String>()
        val ref = storageReference.child("value_goals/$uidUser/$idGoals/image.jpg")

        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    // Insert url data to user
                    databaseReference.child(uidUser).child(idGoals).child("img")
                        .setValue(it.toString())
                    signUpPhotoScreenResult.postValue(it.toString())

                    onRealtimeDbImageGoals.onSuccessImageGoals("success")

                }
            }
            .addOnFailureListener {
                onRealtimeDbImageGoals.onFailureImageGoals(it.message.toString())
                signUpPhotoScreenResult.postValue(null)
            }

        return signUpPhotoScreenResult
    }

    fun deleteGoals(uidUser: String, idGoals: String, imgUrl: String) {
        if (imgUrl.isEmpty()){
            databaseReference.child(uidUser).child(idGoals).removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onRealtimeDbDeleteGoals.onSuccessDeleteGoals("success")
                    } else {
                        onRealtimeDbDeleteGoals.onFailureDeleteGoals(it.exception!!.message.toString())
                    }
                }
        }else{
            firebaseStorage.getReferenceFromUrl(imgUrl).delete().addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    databaseReference.child(uidUser).child(idGoals).removeValue()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                onRealtimeDbDeleteGoals.onSuccessDeleteGoals("success")
                            } else {
                                onRealtimeDbDeleteGoals.onFailureDeleteGoals(it.exception!!.message.toString())
                            }
                        }
                }
            }
        }
    }

    fun removeGoals(uidUser: String, idGoals: String) {
        databaseReference.child(uidUser).child(idGoals).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbDeleteGoals.onSuccessDeleteGoals("success")
            } else {
                onRealtimeDbDeleteGoals.onFailureDeleteGoals(it.exception!!.message.toString())
            }
        }
    }

    fun getAllGoals(uidUser: String) {
        databaseReference.child(uidUser).get()
            .addOnCompleteListener { task ->
                val goals: MutableList<ValueGoals> = ArrayList()

                if (task.isSuccessful) {
                    for (ds in task.result.children) {
                        val goalsList = ds.getValue(ValueGoals::class.java)
                        goals.add(goalsList!!)
                    }
                    Log.d("TAG", "onDataAllGoalsChange: $goals")
                    onRealtimeDbAllValueGoals.onSuccessAllValueGoals(goals)
                } else {
                    Log.d("TAG", task.exception!!.message.toString())
                    onRealtimeDbAllValueGoals.onFailureAllValueGoals(task.exception!!.message.toString())
                }
            }
    }

    fun addTodo(uidUser: String, idGoals: String, todo: String) {
        val add = databaseReference.child(uidUser).child(idGoals).child("ToDo")
        val idTodo = databaseReference.push().key
        val todoAdd = ToDo(idTodo, todo, "false")
        add.child(idTodo!!).setValue(todoAdd).addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbAddTodo.onSuccessAddTodo("success")
            } else {
                onRealtimeDbAddTodo.onFailureAddTodo(it.exception!!.message.toString())
            }
        }
    }

    fun getAllTodo(uidUser: String, idGoals: String) {
        databaseReference.child(uidUser).child(idGoals).child("ToDo")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val todo: MutableList<ToDo> = ArrayList()

                    for (item in snapshot.children) {
                        val toDoList = item.getValue(ToDo::class.java)
                        todo.add(toDoList!!)
                    }
                    Log.d("TAG", "onDataChangeTodo: $todo")
                    onRealtimeDbTodo.onSuccessTodo(todo)
                }

                override fun onCancelled(error: DatabaseError) {
                    onRealtimeDbTodo.onFailureTodo(error)
                }
            })
    }

    fun doneTodo(uidUser: String, idGoals: String, idTodo: String) {
        databaseReference.child(uidUser).child(idGoals).child("ToDo").child(idTodo)
            .child("completed").setValue("true").addOnCompleteListener {
                if (it.isSuccessful) {
                    onRealtimeDbDoneTodo.onSuccessDoneTodo("success")
                } else {
                    onRealtimeDbDoneTodo.onFailureDoneTodo(it.exception!!.message.toString())
                }
            }
    }

    fun notDoneTodo(uidUser: String, idGoals: String, idTodo: String) {
        databaseReference.child(uidUser).child(idGoals).child("ToDo").child(idTodo)
            .child("completed").setValue("false").addOnCompleteListener {
                if (it.isSuccessful) {
                    onRealtimeDbNotDoneTodo.onSuccessNotDoneTodo("success")
                } else {
                    onRealtimeDbNotDoneTodo.onFailureNotDoneTodo(it.exception!!.message.toString())
                }
            }
    }

    fun deleteTodo(uidUser: String, idGoals: String, idTodo: String) {
        databaseReference.child(uidUser).child(idGoals).child("ToDo").child(idTodo).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                onRealtimeDbDeleteTodo.onFailureDeleteTodo("success")
            } else {
                onRealtimeDbDeleteGoals.onFailureDeleteGoals(it.exception!!.message.toString())
            }
        }
    }

    interface OnRealtimeDbAddValueGoals {
        fun onSuccessAddValueGoals(idGoals: String?)
        fun onFailureAddValueGoals(error: String?)
    }

    interface OnRealtimeDbImageGoals {
        fun onSuccessImageGoals(status: String?)
        fun onFailureImageGoals(error: String?)
    }

    interface OnRealtimeDbDeleteGoals {
        fun onSuccessDeleteGoals(status: String?)
        fun onFailureDeleteGoals(error: String?)
    }

    interface OnRealtimeDbUpdateGoals {
        fun onSuccessUpdateGoals(status: String?)
        fun onFailureUpdateGoals(error: String?)
    }

    interface OnRealtimeDbAllValueGoals {
        fun onSuccessAllValueGoals(valueGoals: List<ValueGoals>?)
        fun onFailureAllValueGoals(error: String?)
    }


    interface OnRealtimeDbAddTodo {
        fun onSuccessAddTodo(status: String?)
        fun onFailureAddTodo(error: String?)
    }

    interface OnRealtimeDbDoneTodo {
        fun onSuccessDoneTodo(status: String?)
        fun onFailureDoneTodo(error: String?)
    }

    interface OnRealtimeDbNotDoneTodo {
        fun onSuccessNotDoneTodo(status: String?)
        fun onFailureNotDoneTodo(error: String?)
    }

    interface OnRealtimeDbTodo {
        fun onSuccessTodo(todo: List<ToDo>?)
        fun onFailureTodo(error: DatabaseError?)
    }

    interface OnRealtimeDbDeleteTodo {
        fun onSuccessDeleteTodo(status: String?)
        fun onFailureDeleteTodo(error: String?)
    }
}