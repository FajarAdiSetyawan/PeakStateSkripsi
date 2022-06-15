package com.brainoptimax.peakmeup.viewmodel.anchoring

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brainoptimax.peakmeup.model.anchoring.Anchoring
import com.brainoptimax.peakmeup.model.anchoring.Memory
import com.brainoptimax.peakmeup.model.anchoring.Resourceful
import com.brainoptimax.peakmeup.repository.AnchoringRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class AnchoringViewModel : ViewModel(),
    AnchoringRepository.OnRealtimeDbAddAnchoring,
    AnchoringRepository.OnRealtimeDbAddMemory,
    AnchoringRepository.OnRealtimeDbAddResourceful,
    AnchoringRepository.OnRealtimeDbAnchoring,
    AnchoringRepository.OnRealtimeDbResourceful,
    AnchoringRepository.OnRealtimeDbMemory
{
    val addResourcefulLiveData = MutableLiveData<String?>()
    val databaseErrorAddResourceful = MutableLiveData<String?>()

    val addMemoryLiveData = MutableLiveData<String?>()
    val databaseErrorAddMemory = MutableLiveData<String?>()

    val addAnchoringLiveData = MutableLiveData<String?>()
    val databaseErrorAddAnchoring = MutableLiveData<String?>()

    val anchroingMutableLiveData = MutableLiveData<List<Anchoring>?>()
    val databaseErrorAnchroing = MutableLiveData<DatabaseError?>()

    val resourcefulMutableLiveData = MutableLiveData<List<Resourceful>?>()
    val databaseErrorResourceful = MutableLiveData<DatabaseError?>()

    val memoryMutableLiveData = MutableLiveData<List<Memory>?>()
    val databaseErrorMemory = MutableLiveData<DatabaseError?>()

    var status = MutableLiveData<Boolean?>()

    private val anchoringRepository: AnchoringRepository = AnchoringRepository(this, this, this, this, this, this, )

//    fun addAnchoring(
//        ref: DatabaseReference,
//        view: View?,
//        id: String,
//        resourceful: String?,
//        memory: String?,
//        descMemory: String?,
//        dateTime: String?
//    ) {
//        val anchoring = Anchoring(id, resourceful, memory, descMemory, dateTime)
//        ref.setValue(anchoring).addOnCompleteListener {
//            if (it.isSuccessful) {
//                status.value = true
//            } else {
//                // tampilkan dialog error
//                val message: String =
//                    it.exception!!.message.toString() // mengambil pesan error
//                Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }

//    fun addResourceful(
//        ref: DatabaseReference,
//        view: View?,
//        id: String,
//        resourceful: String?
//    ) {
//        val resourcefulAdd = Resourceful(id, resourceful)
//        ref.child(id).setValue(resourcefulAdd).addOnCompleteListener {
//            if (it.isSuccessful) {
//                status.value = true
//            } else {
//                // tampilkan dialog error
//                val message: String =
//                    it.exception!!.message.toString() // mengambil pesan error
//                Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
//                    .show()
//            }
//        }
//    }

    fun addMemory(
        uid: String,
        memory: String?
    ) {
        anchoringRepository.addMemory(uid, memory!!)
    }

    fun addResourceful(
        uid: String,
        resourceful: String?
    ) {
        anchoringRepository.addResourceful(uid, resourceful!!)
    }

    fun addAnchoring(
        uid: String,
        memory: String?,
        resourceful: String?,
        note: String,
        currentDateTime: String
    ) {
        anchoringRepository.addAnchoring(uid, resourceful!!, memory!!, note, currentDateTime)
    }

    // TODO mengambil semua data dari firebase yg di kirim dari repository

    fun allAnchoring(uid:String){
        anchoringRepository.getAnchoring(uid)
    }

    fun allResourceful(uid:String){
        anchoringRepository.getResourceful(uid)
    }

    fun allMemory(uid:String){
        anchoringRepository.getMemory(uid)
    }

    // TODO menghapus data
    fun deleteResourceful(uid:String, id: String) {
        anchoringRepository.deleteResourceful(uid, id)
    }

    fun deleteMemory(uid:String, id: String) {
        anchoringRepository.deleteMemory(uid, id)
    }

    fun deleteAnchoring(uid:String, id: String) {
        anchoringRepository.deleteAnchoring(uid, id)
    }

    // TODO menjadikan data yg sudah diambil menjadi list
    override fun onSuccess(anchoring: List<Anchoring>?) {
        anchroingMutableLiveData.value = anchoring
    }

    // TODO mengambil jika terjadi error
    override fun onFailure(error: DatabaseError?) {
        databaseErrorAnchroing.value = error
    }

    override fun onSuccessResourceful(resourceful: List<Resourceful>?) {
        resourcefulMutableLiveData.value = resourceful
    }

    override fun onFailureResourceful(error: DatabaseError?) {
        databaseErrorResourceful.value = error
    }

    override fun onSuccessMemory(memory: List<Memory>?) {
       memoryMutableLiveData.value = memory
    }

    override fun onFailureMemory(error: DatabaseError?) {
        databaseErrorMemory.value = error
    }

    override fun onSuccessAddMemory(status: String?) {
        addMemoryLiveData.value = status
    }

    override fun onFailureAddMemory(error: String?) {
        databaseErrorAddMemory.value = error
    }

    override fun onSuccessAddResourceful(status: String?) {
        addResourcefulLiveData.value = status
    }

    override fun onFailureAddResourceful(error: String?) {
        databaseErrorAddResourceful.value = error
    }

    override fun onSuccessAddAnchoring(status: String?) {
        addAnchoringLiveData.value = status
    }

    override fun onFailureAddAnchoring(error: String?) {
        databaseErrorAddAnchoring.value = error
    }
}