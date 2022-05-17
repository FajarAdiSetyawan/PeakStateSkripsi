package com.brainoptimax.peakstate.viewmodel.bottomnav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.brainoptimax.peakstate.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log

class HomeViewModel: ViewModel()