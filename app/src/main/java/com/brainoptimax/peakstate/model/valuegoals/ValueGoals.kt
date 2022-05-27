package com.brainoptimax.peakstate.model.valuegoals

//data class ValueGoals (
//    var id: String? = null,
//    var value: String? = null,
//    var statement: String? = null,
//    var date: String? = null,
//    var time: String? = null,
//    var dateTime: String? = null,
//    var descValue: String? = null,
//    var imgValue: String? = null,
//    var goals: MutableList<Goals> = ArrayList()
//): Serializable

class ValueGoals {
    var id: String? = null
    var value: String? = null
    var statement: String? = null
    var time: String? = null
    var date: String? = null
    var descValue: String? = null
    var img: String? = null
    var goals: MutableList<ToDo> = ArrayList()

    constructor()


    constructor(goals: MutableList<ToDo>) {
        this.goals = goals
    }

    constructor(id: String?, value: String?, statement: String?, time: String?, date: String?, descValue: String?, img: String?){
        this.id = id
        this.value = value
        this.statement = statement
        this.time = time
        this.date = date
        this.descValue = descValue
        this.img= img
    }
}