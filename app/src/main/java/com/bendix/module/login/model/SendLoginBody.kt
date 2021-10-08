package com.bendix.module.login.model

class SendLoginBody() {
    var login: String = ""
    var password: String = ""
    var db: String = ""

    constructor(login: String, password: String) : this() {
        this.login = login
        this.password = password
        this.db = "erpwave_12.0"
    }
}