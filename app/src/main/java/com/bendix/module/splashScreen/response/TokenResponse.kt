package com.bendix.module.splashScreen.response

import com.kotlinmvvmstructure.genericResponse.BaseResponse
import org.json.JSONObject

class TokenResponse: BaseResponse() {

    var data: JSONObject? = null
}