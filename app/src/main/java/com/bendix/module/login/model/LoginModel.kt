package com.bendix.module.login.model

import com.bendix.module.login.response.LoginResponse
import org.json.JSONObject
import java.io.Serializable

class LoginModel(): Serializable {

    var data: JSONObject = JSONObject()


    constructor(response: LoginResponse): this(){

        if (response != null)
            this.data = response.data!!
    }
}