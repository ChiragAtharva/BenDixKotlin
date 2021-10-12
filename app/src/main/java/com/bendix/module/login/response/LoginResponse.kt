package com.bendix.module.login.response

import com.kotlinmvvmstructure.genericResponse.BaseResponse

class LoginResponse: BaseResponse() {
    var uid: String? = null
    var company_id: String? = null
    var partner_id: String? = null
    var access_token: String? = null
}