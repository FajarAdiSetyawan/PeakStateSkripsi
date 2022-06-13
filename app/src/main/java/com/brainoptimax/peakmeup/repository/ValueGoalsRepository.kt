package com.brainoptimax.peakmeup.repository

import android.util.Log
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.model.valuegoals.ValueGoals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ValueGoalsRepository(
    private val onRealtimeDbTaskComplete: OnRealtimeDbTaskComplete,
    private val onRealtimeDbTaskCompleteTodo: OnRealtimeDbTaskCompleteTodo
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid).child("ValueGoals")

    fun getAllGoals() {
        databaseReference.get()
            .addOnCompleteListener { task ->
                val goals: MutableList<ValueGoals> = ArrayList()

                if (task.isSuccessful) {
                    for (ds in task.result.children) {
                        val goalsList = ds.getValue(ValueGoals::class.java)
                        goals.add(goalsList!!)
                    }
                    Log.d("TAG", "onDataChange: $goals")
                    onRealtimeDbTaskComplete.onSuccess(goals)
                } else {
                    Log.d("TAG", task.exception!!.message.toString()) //Don't ignore potential errors!
                    onRealtimeDbTaskComplete.onFailure(task.exception!!.message.toString())
                }
            }
    }

    fun getAllTodo(idGoals: String) {
        databaseReference.child(idGoals).child("ToDo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todo: MutableList<ToDo> = ArrayList()

                for (item in snapshot.children) {
                    val toDoList = item.getValue(ToDo::class.java)
                    todo.add(toDoList!!)
                }
                Log.d("TAG", "onDataChangeTodo: $todo")
                onRealtimeDbTaskCompleteTodo.onSuccessTodo(todo)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbTaskCompleteTodo.onFailureTodo(error)
            }
        })
    }


    interface OnRealtimeDbTaskComplete {
        fun onSuccess(valueGoals: List<ValueGoals>?)
        fun onFailure(error: String?)
    }

    interface OnRealtimeDbTaskCompleteTodo {
        fun onSuccessTodo(todo: List<ToDo>?)
        fun onFailureTodo(error: DatabaseError?)
    }
}