package com.bendix.module.product.model

class AskQuotationRequestBody() {
    var token: String = ""
    var uid: String = ""
    var order_id: String = ""

    constructor(
        token: String,
        uid: String,
        orderId: String,
    ) : this() {
        this.uid = uid
        this.token = token
        this.order_id = orderId
    }
}