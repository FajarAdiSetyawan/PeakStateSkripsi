package com.brainoptimax.peakmeup.model

class Users {
    var username: String? = null
    var email: String? = null
    var id: String? = null
    var fullname: String? = null
    var photoUrl: String? = null

    constructor()
    constructor(id: String?, email: String?, username: String?) {
        this.id = id
        this.email = email
        this.username = username
    }

    constructor(id: String?, email: String?, username: String?, fullname: String?, photoUrl: String?) {
        this.id = id
        this.email = email
        this.username = username
        this.fullname = fullname
        this.photoUrl = photoUrl
    }

    constructor(id: String?, email: String?, username: String?, photoUrl: String?) {
        this.id = id
        this.email = email
        this.username = username
        this.photoUrl = photoUrl
    }
}