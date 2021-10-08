package com.bendix.webservice.interfaces

import com.bendix.module.login.model.SendLoginBody
import com.bendix.module.login.response.LoginResponse
import com.bendix.module.scanCustomerBarcode.model.SendTokenBody
import com.bendix.module.scanCustomerBarcode.response.TokenResponse
import com.bendix.webservice.APIConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("/${APIConstants.MODULE_AUTH}/${APIConstants.SERVICE_TOKEN}")
    fun sendLogin(@Body sendLoginOtpBody: SendLoginBody): Call<LoginResponse>

    @POST("/${APIConstants.SERVICE_TOKEN_VALIDATION}")
    fun validateToken(@Body sendTokenBody: SendTokenBody): Call<TokenResponse>
}