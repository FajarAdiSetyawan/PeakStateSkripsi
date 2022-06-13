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
    AnchoringRepository.OnRealtimeDbAnchoring,
    AnchoringRepository.OnRealtimeDbResourceful,
    AnchoringRepository.OnRealtimeDbMemory
{

    val anchroingMutableLiveData = MutableLiveData<List<Anchoring>?>()
    val databaseErrorAnchroing = MutableLiveData<DatabaseError?>()

    val resourcefulMutableLiveData = MutableLiveData<List<Resourceful>?>()
    val databaseErrorResourceful = MutableLiveData<DatabaseError?>()

    val memoryMutableLiveData = MutableLiveData<List<Memory>?>()
    val databaseErrorMemory = MutableLiveData<DatabaseError?>()

    var status = MutableLiveData<Boolean?>()

    private val anchoringRepository: AnchoringRepository = AnchoringRepository(this, this, this)

    fun addAnchoring(
        ref: DatabaseReference,
        view: View?,
        id: String,
        resourceful: String?,
        memory: String?,
        descMemory: String?,
        dateTime: String?
    ) {
        val anchoring = Anchoring(id, resourceful, memory, descMemory, dateTime)
        ref.setValue(anchoring).addOnCompleteListener {
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

    fun addResourceful(
        ref: DatabaseReference,
        view: View?,
        id: String,
        resourceful: String?
    ) {
        val resourcefulAdd = Resourceful(id, resourceful)
        ref.child(id).setValue(resourcefulAdd).addOnCompleteListener {
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

    fun addMemory(
        ref: DatabaseReference,
        view: View?,
        id: String,
        memory: String?
    ) {
        val memoryAdd = Memory(id, memory)
        ref.child(id).setValue(memoryAdd).addOnCompleteListener {
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


    // TODO mengambil semua data dari firebase yg di kirim dari repository
    val allAnchoring: Unit
        get() {
            anchoringRepository.getAnchoring()
        }

    val allResourceful: Unit
        get() {
            anchoringRepository.getResourceful()
        }

    val allMemory: Unit
        get() {
            anchoringRepository.getMemory()
        }

    // TODO menghapus data
    fun deleteResourceful(id: String) {
        anchoringRepository.deleteResourceful(id)
    }

    fun deleteMemory(id: String) {
        anchoringRepository.deleteMemory(id)
    }

    fun deleteAnchoring(id: String) {
        anchoringRepository.deleteAnchoring(id)
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
}