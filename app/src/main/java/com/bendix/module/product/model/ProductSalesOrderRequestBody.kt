package com.bendix.module.product.model

class ProductSalesOrderRequestBody() {
    var token: String = ""
    var barcode: String = ""
    var uid: String = ""
    var order_id: String = ""

    constructor(token: String, uid: String, barcode: String, orderId: String) : this() {
        this.uid = uid
        this.token = token
        this.barcode = barcode
        this.order_id = orderId
    }
}