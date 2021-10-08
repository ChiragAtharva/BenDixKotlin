package com.bendix.module.login.response

import com.kotlinmvvmstructure.genericResponse.BaseResponse
import org.json.JSONObject

class LoginResponse: BaseResponse() {

    var data: JSONObject? = null
}