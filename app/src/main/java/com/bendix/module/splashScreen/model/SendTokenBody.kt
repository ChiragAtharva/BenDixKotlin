package com.bendix.module.splashScreen.model

class SendTokenBody() {
    var uid: String = ""
    var token: String = ""

    constructor(uid: String, token: String) : this() {
        this.uid = uid
        this.token = token
    }
}