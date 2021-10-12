package com.bendix.module.product.model

import com.bendix.module.product.response.ProductInfoResponse
import org.json.JSONArray

class ProductInfoModel() {
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
    var type: String? = null
    var message: String? = null

    constructor(response: ProductInfoResponse) : this() {

        if (response.id != null)
            this.id = response.id!!
        if (response.name != null)
            this.name = response.name!!
        if (response.product_id != null)
            this.product_id = response.product_id!!
        if (response.uid != null)
            this.uid = response.uid!!
        if (response.saleorderline_id != null)
            this.saleorderline_id = response.saleorderline_id!!
        if (response.quantity != null)
            this.quantity = response.quantity!!
        if (response.price != null)
            this.price = response.price!!
        if (response.qty != null)
            this.qty = response.qty!!
        if (response.order != null)
            this.order = response.order!!
        if (response.products != null)
            this.products = response.products!!
        if (response.description != null)
            this.description = response.description!!
        if (response.image_path != null)
            this.image_path = response.image_path!!
        if (response.sub_total != null)
            this.sub_total = response.sub_total!!
        if (response.tax_amount != null)
            this.tax_amount = response.tax_amount!!
        if (response.total != null)
            this.total = response.total!!
        if (response.type != null)
            this.type = response.type!!
        if (response.message != null)
            this.message = response.message!!
    }
}