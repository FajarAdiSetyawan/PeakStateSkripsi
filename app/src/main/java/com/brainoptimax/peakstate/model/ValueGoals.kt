package com.brainoptimax.peakstate.model

data class ValueGoals (
    var id: String? = null,
    var value: String? = null,
    var statement: String? = null,
    var date: String? = null,
    var time: String? = null,
    var dateTime: String? = null,
    var descValue: String? = null,
    var imgValue: String? = null,
    var goals: MutableList<Goals> = ArrayList()
)

//class ValueGoals {
//    var id: String? = null
//    var value: String? = null
//    var statement: String? = null
//    var time: String? = null
//    var date: String? = null
//    var descValue: String? = null
//    var goals: MutableList<Goals> = ArrayList()
//
//    constructor()
//
//
//    constructor(goals: MutableList<Goals>) {
//        this.goals = goals
//    }
//
//    constructor(id: String?, value: String?, statement: String?, time: String?, date: String?, descValue: String?){
//        this.id = id
//        this.value = value
//        this.statement = statement
//        this.time = time
//        this.date = date
//        this.descValue = descValue
//    }
//}