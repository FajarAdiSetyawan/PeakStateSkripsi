package com.brainoptimax.peakstate.model

class Emotion{
    var emotionName: String? = null
    var emotionNote: String? = null
    var emotionDate: String? = null
    var totalAllEmotion: Int? = null
    var totalPerEmotion: Int? = null

    constructor()

    constructor(emotionName: String?, totalAllEmotion: Int?, totalPerEmotion: Int?) {
        this.emotionName = emotionName
        this.totalAllEmotion = totalAllEmotion
        this.totalPerEmotion = totalPerEmotion
    }

    constructor(emotionName: String?, emotionNote: String?, emotionDate: String?) {
        this.emotionName = emotionName
        this.emotionNote = emotionNote
        this.emotionDate = emotionDate
    }
}

