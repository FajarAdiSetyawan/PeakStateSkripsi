package com.brainoptimax.peakstate.model

import java.io.Serializable

class Reminders(
    var id: Long = 0,
    var title: String = " ",
    var subtitle: String = " ",
    var description: String = " ",
    var time: String = " ",
    var date: String = " ",
    var createdTime: Long = 0,
    var modifiedTime: Long = 0
) : Serializable
