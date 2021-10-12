package com.bendix.module.login.model

import com.bendix.module.login.response.LoginResponse
import java.io.Serializable

class LoginModel(): Serializable {
    var uid: String? = null
    var company_id: String? = null
    var partner_id: String? = null
    var access_token: String? = null
    var type: String? = null
    var message: String? = null

    constructor(response: LoginResponse): this(){

        if (response.uid != null)
            this.uid = response.uid!!
        if (response.company_id != null)
            this.company_id = response.company_id!!
        if (response.partner_id != null)
            this.partner_id = response.partner_id!!
        if (response.access_token != null)
            this.access_token = response.access_token!!
        if (response.type != null)
            this.type = response.type!!
        if (response.message != null)
            this.message = response.message!!
    }
}