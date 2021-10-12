package com.bendix.module.product.model

class ProductInfoRequestBody() {
    var token: String = ""
    var barcode: String = ""
    var uid: String = ""

    constructor(token: String, uid: String, barcode: String) : this() {
        this.uid = uid
        this.token = token
        this.barcode = barcode
    }
}