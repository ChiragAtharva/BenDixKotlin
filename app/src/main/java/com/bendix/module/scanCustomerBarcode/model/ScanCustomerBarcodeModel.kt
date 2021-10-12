package com.bendix.module.scanCustomerBarcode.model

import com.bendix.module.scanCustomerBarcode.response.CustomerResponse
import java.io.Serializable

class ScanCustomerBarcodeModel(): Serializable {

    var id: String = ""
    var name: String = ""
    var pricelist: String = ""
    var order_id: String = ""
    var type: String? = null
    var message: String? = null

    constructor(response: CustomerResponse): this(){

        if (response.id != null)
            this.id = response.id!!
        if (response.name != null)
            this.name = response.name!!
        if (response.pricelist != null)
            this.pricelist = response.pricelist!!
        if (response.order_id != null)
            this.order_id = response.order_id!!
        if (response.type != null)
            this.type = response.type!!
        if (response.message != null)
            this.message = response.message!!
    }
}