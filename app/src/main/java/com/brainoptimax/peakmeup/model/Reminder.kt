package com.brainoptimax.peakmeup.model

data class Reminder (
    var idReminder: String? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var description: String? = null,
    var date: String? = null,
    var time: String? = null,
    var imgurl: String? = null,
)