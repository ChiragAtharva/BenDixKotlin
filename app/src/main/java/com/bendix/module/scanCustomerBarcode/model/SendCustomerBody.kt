package com.bendix.module.scanCustomerBarcode.model

class SendCustomerBody() {
    var uid: String = ""
    var token: String = ""
    var barcode: String = ""

    constructor(uid: String, token: String, barcode: String) : this() {
        this.uid = uid
        this.token = token
        this.barcode = barcode
    }
}