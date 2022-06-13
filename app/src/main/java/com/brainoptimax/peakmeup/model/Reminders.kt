package com.brainoptimax.peakmeup.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "reminder_table")
class Reminders: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "reminder_title")
    var title: String = ""

    @ColumnInfo(name = "reminder_subtitle")
    var subtitle: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "datetime")
    var datetime = ""

    @ColumnInfo(name = "date")
    var date = ""

    @ColumnInfo(name = "time")
    var time = ""
}

