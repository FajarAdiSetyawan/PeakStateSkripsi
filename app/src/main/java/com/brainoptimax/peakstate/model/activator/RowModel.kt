package com.brainoptimax.peakstate.model.activator

import android.os.Parcelable
import androidx.annotation.IntDef

class RowModel {
    companion object {

        @IntDef(ACTIVATOR, EXPAND_ACTIVATOR, DETAIL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class RowType

        const val ACTIVATOR = 1
        const val EXPAND_ACTIVATOR = 2
        const val DETAIL = 3
    }

    @RowType var type : Int

    lateinit var activator: Activator

    lateinit var expandActivator: ExpandActivator

    lateinit var detailActivator: DetailActivator

    var isExpanded : Boolean

    constructor(@RowType type : Int, activator: Activator , isExpanded : Boolean = false){
        this.type = type
        this.activator = activator
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, expandActivator: ExpandActivator, isExpanded : Boolean = false){
        this.type = type
        this.expandActivator = expandActivator
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, detailActivator: DetailActivator, isExpanded : Boolean = false){
        this.type = type
        this.detailActivator = detailActivator
        this.isExpanded = isExpanded
    }
}