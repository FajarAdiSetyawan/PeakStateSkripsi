package com.brainoptimax.peakmeup.viewmodel.valuegoals

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.brainoptimax.peakmeup.R
import com.brainoptimax.peakmeup.model.valuegoals.ToDo
import com.brainoptimax.peakmeup.model.valuegoals.ValueGoals
import com.brainoptimax.peakmeup.repository.ValueGoalsRepository
import com.google.firebase.database.DatabaseError


class ValueGoalsViewModel : ViewModel(),
    ValueGoalsRepository.OnRealtimeDbAddValueGoals,
    ValueGoalsRepository.OnRealtimeDbAllValueGoals,
    ValueGoalsRepository.OnRealtimeDbImageGoals,
    ValueGoalsRepository.OnRealtimeDbDeleteGoals,
    ValueGoalsRepository.OnRealtimeDbUpdateGoals,
    ValueGoalsRepository.OnRealtimeDbTodo,
    ValueGoalsRepository.OnRealtimeDbDoneTodo,
    ValueGoalsRepository.OnRealtimeDbNotDoneTodo,
    ValueGoalsRepository.OnRealtimeDbAddTodo,
    ValueGoalsRepository.OnRealtimeDbDeleteTodo
{

    val idGoalsMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddGoals = MutableLiveData<String?>()

    val imageGoalsMutableLiveData = MutableLiveData<String?>()
    val databaseErrorImageGoals = MutableLiveData<String?>()

    val deleteGoalsMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDeleteGoals = MutableLiveData<String?>()

    val updateGoalsMutableLiveData = MutableLiveData<String?>()
    val databaseErrorUpdateGoals = MutableLiveData<String?>()

    val goalsMutableLiveData = MutableLiveData<List<ValueGoals>?>()
    val databaseErrorGoals = MutableLiveData<String?>()

    val addTodoMutableLiveData = MutableLiveData<String?>()
    val databaseErrorAddTodo = MutableLiveData<String?>()

    val todoMutableLiveData = MutableLiveData<List<ToDo>?>()
    val databaseErrorToDo = MutableLiveData<DatabaseError?>()

    val doneTodoMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDoneTodo = MutableLiveData<String?>()

    val notDoneTodoMutableLiveData = MutableLiveData<String?>()
    val databaseErrorNotDoneTodo = MutableLiveData<String?>()

    val deleteTodoMutableLiveData = MutableLiveData<String?>()
    val databaseErrorDeleteTodo = MutableLiveData<String?>()

    private lateinit var alertDialogLoading: AlertDialog

    private val valueGoalsRepository: ValueGoalsRepository = ValueGoalsRepository(this,this,this, this, this,this, this, this, this, this)

    var status = MutableLiveData<Boolean?>()

    fun allGoals(uidUser: String) {
        valueGoalsRepository.getAllGoals(uidUser)
    }

    fun allToDo(uidUser: String, idGoals: String) {
        valueGoalsRepository.getAllTodo(uidUser, idGoals)
    }

    fun addGoal(
        uidUser: String,
        value: String,
        state: String,
        date: String,
        time: String,
        desc: String,
    ) {
        valueGoalsRepository.addValueGoals(uidUser, value, state, date, time, desc)
    }

    fun updateGoal(
        uidUser: String,
        idGoals: String,
        value: String,
        state: String,
        date: String,
        time: String,
        desc: String
    ) {
        valueGoalsRepository.updateValueGoals(uidUser, idGoals,value, state, date, time, desc)
    }

    fun setImageGoals(uidUser: String, idGoals: String, filePath: Uri){
        valueGoalsRepository.setImgValueGoal(uidUser, idGoals, filePath)
    }

    fun deleteGoal(
        uidUser: String,
        idGoals: String,
        imgUrl: String
    ) {
        valueGoalsRepository.deleteGoals(uidUser, idGoals, imgUrl)
    }

    fun removeGoal(
        uidUser: String,
        idGoals: String,
    ) {
        valueGoalsRepository.removeGoals(uidUser, idGoals)
    }

    fun addToDoList(
        uidUser: String,
        idGoals: String,
        todo: String,
    ) {
        valueGoalsRepository.addTodo(uidUser, idGoals, todo)
    }

    fun doneTodo(uidUser: String, idGoals: String, idTodo: String){
        valueGoalsRepository.doneTodo(uidUser, idGoals, idTodo)
    }

    fun notDoneTodo(uidUser: String, idGoals: String, idTodo: String){
        valueGoalsRepository.notDoneTodo(uidUser, idGoals, idTodo)
    }

    fun deleteTodo(uidUser: String, idGoals: String, idTodo: String){
        valueGoalsRepository.deleteTodo(uidUser, idGoals, idTodo)
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
        val lottie =
            dialogView.findViewById<View>(R.id.lottie_dialog_loading) as LottieAnimationView
        lottie.playAnimation()
        lottie.setAnimation(R.raw.upload)
        alertDialogLoading.window!!.setBackgroundDrawable(inset)
        alertDialogLoading.show()
    }

    fun closeLoadingDialog() {
        alertDialogLoading.dismiss()
    }

    override fun onSuccessAddValueGoals(idGoals: String?) {
        idGoalsMutableLiveData.value = idGoals
    }

    override fun onFailureAddValueGoals(error: String?) {
        databaseErrorAddGoals.value = error
    }

    override fun onSuccessAllValueGoals(valueGoals: List<ValueGoals>?) {
        goalsMutableLiveData.value = valueGoals
    }

    override fun onFailureAllValueGoals(error: String?) {
        databaseErrorGoals.value = error
    }

    override fun onSuccessAddTodo(status: String?) {
        addTodoMutableLiveData.value = status
    }

    override fun onFailureAddTodo(error: String?) {
        databaseErrorAddTodo.value = error
    }

    override fun onSuccessImageGoals(status: String?) {
        imageGoalsMutableLiveData.value = status
    }

    override fun onFailureImageGoals(error: String?) {
        databaseErrorImageGoals.value = error
    }

    override fun onSuccessTodo(todo: List<ToDo>?) {
        todoMutableLiveData.value = todo
    }

    override fun onFailureTodo(error: DatabaseError?) {
        databaseErrorToDo.value = error!!
    }

    override fun onSuccessDeleteGoals(status: String?) {
        deleteGoalsMutableLiveData.value = status
    }

    override fun onFailureDeleteGoals(error: String?) {
        databaseErrorDeleteGoals.value = error
    }

    override fun onSuccessDoneTodo(status: String?) {
        doneTodoMutableLiveData.value = status
    }

    override fun onFailureDoneTodo(error: String?) {
        databaseErrorDoneTodo.value = error
    }

    override fun onSuccessNotDoneTodo(status: String?) {
        notDoneTodoMutableLiveData.value = status
    }

    override fun onFailureNotDoneTodo(error: String?) {
        databaseErrorNotDoneTodo.value = error
    }

    override fun onSuccessUpdateGoals(status: String?) {
        updateGoalsMutableLiveData.value = status
    }

    override fun onFailureUpdateGoals(error: String?) {
        databaseErrorUpdateGoals.value = error
    }

    override fun onSuccessDeleteTodo(status: String?) {
        deleteTodoMutableLiveData.value = status
    }

    override fun onFailureDeleteTodo(error: String?) {
        databaseErrorDeleteTodo.value = error
    }

}