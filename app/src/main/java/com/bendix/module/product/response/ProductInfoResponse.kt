package com.bendix.module.product.response

import com.kotlinmvvmstructure.genericResponse.BaseResponse
import org.json.JSONArray

class ProductInfoResponse : BaseResponse() {
    var id: String? = null
    var product_id: String? = null
    var uid: String? = null
    var name: String? = null
    var saleorderline_id: String? = null
    var quantity: String? = null
    var price: String? = null
    var qty: String? = null
    var order: String? = null
    var products: JSONArray? = null
    var description: String? = null
    var image_path: String? = null
    var sub_total: String? = null
    var tax_amount: String? = null
    var total: String? = null
    //var message: String? = null
}