package com.bendix.module.scanCustomerBarcode.response

import com.kotlinmvvmstructure.genericResponse.BaseResponse

class CustomerResponse : BaseResponse() {

    //var data: JSONObject? = null
    var id: String? = null
    var name: String? = null
    var pricelist: String? = null
    var order_id: String? = null
}