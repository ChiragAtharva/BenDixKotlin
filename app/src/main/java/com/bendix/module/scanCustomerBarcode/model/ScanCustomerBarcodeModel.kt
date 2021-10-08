package com.bendix.module.scanCustomerBarcode.model

import com.bendix.module.scanCustomerBarcode.response.TokenResponse
import org.json.JSONObject
import java.io.Serializable

class ScanCustomerBarcodeModel(): Serializable {

    var data: JSONObject = JSONObject()


    constructor(response: TokenResponse): this(){

        if (response != null)
            this.data = response.data!!
    }
}