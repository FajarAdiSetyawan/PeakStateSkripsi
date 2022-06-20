package com.brainoptimax.peakmeup.model

class Quiz {
    var idQuiz: String? = null
    var peakQuiz: String? = null
    var energyQuiz: String? = null
    var tensionQuiz: String? = null
    var datetime: String? = null


    constructor()
    constructor(idQuiz: String?, peakQuiz: String?, datetime: String?) {
        this.idQuiz = idQuiz
        this.peakQuiz = peakQuiz
        this.datetime = datetime
    }

    constructor(idQuiz: String?, energyQuiz: String?, tensionQuiz: String?, datetime: String?) {
        this.idQuiz = idQuiz
        this.energyQuiz = energyQuiz
        this.tensionQuiz = tensionQuiz
        this.datetime = datetime
    }
}