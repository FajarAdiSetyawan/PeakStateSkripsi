package com.brainoptimax.peakstate.model.activator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DetailActivator(
    val iso : String,
    val music: String,
    val breathing : String,
    val suggest: String
    ): Parcelable