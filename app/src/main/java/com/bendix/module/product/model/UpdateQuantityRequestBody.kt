package com.bendix.module.product.model

class UpdateQuantityRequestBody() {
    var token: String = ""
    var line_id: String = ""
    var uid: String = ""
    var order_id: String = ""
    var qty: String = ""

    constructor(
        token: String,
        uid: String,
        saleOrderLineId: String,
        orderId: String,
        qty: String
    ) : this() {
        this.uid = uid
        this.token = token
        this.line_id = saleOrderLineId
        this.order_id = orderId
        this.qty = qty
    }
}