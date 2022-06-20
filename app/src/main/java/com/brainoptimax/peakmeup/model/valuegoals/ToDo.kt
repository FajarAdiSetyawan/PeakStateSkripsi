package com.brainoptimax.peakmeup.model.valuegoals

data class ToDo(
    var idTodo: String?,
    var todo: String?,
    var isCompleted: String?
){
    constructor(): this("", "", "")
}


//class Goals{
//    var id: String? = null
//    var goals: String? = null
//    var isCompleted: Boolean = false
//
//    constructor()
//    constructor(goals: String?, isCompleted: Boolean) {
//        this.goals = goals
//        this.isCompleted = isCompleted
//    }
//
//    constructor(id: String?, goals: String?, isCompleted: Boolean) {
//        this.id = id
//        this.goals = goals
//        this.isCompleted = isCompleted
//    }
//}