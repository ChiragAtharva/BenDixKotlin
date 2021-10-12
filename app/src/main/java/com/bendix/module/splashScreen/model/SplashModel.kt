package com.bendix.module.splashScreen.model

import com.bendix.module.splashScreen.response.TokenResponse
import java.io.Serializable

class SplashModel(): Serializable {

    //var Token: String = ""
    var Expired: String = ""
    var type: String? = null
    var message: String? = null

    constructor(response: TokenResponse): this(){

        //if (response.token != null)
        //    this.Token = response.Token!!
        if (response.Expired != null)
            this.Expired = response.Expired!!
        if (response.type != null)
            this.type = response.type!!
        if (response.message != null)
            this.message = response.message!!

    }
}