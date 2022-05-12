package com.brainoptimax.peakstate.model

class Emotion{
    var emotionName: String? = null
    var emotionNote: String? = null
    var emotionDate: String? = null
    var emotionTotal: Int? = null

    constructor()

    constructor(emotionName: String?, emotionTotal: Int?) {
        this.emotionName = emotionName
        this.emotionTotal = emotionTotal
    }

    constructor(emotionName: String?, emotionNote: String?, emotionDate: String?) {
        this.emotionName = emotionName
        this.emotionNote = emotionNote
        this.emotionDate = emotionDate
    }
}

