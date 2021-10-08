package com.bendix.module.splashScreen.model

import com.bendix.module.scanCustomerBarcode.response.TokenResponse
import org.json.JSONObject
import java.io.Serializable

class SplashModel(): Serializable {

    var data: JSONObject = JSONObject()


    constructor(response: TokenResponse): this(){

        if (response != null)
            this.data = response.data!!
    }
}