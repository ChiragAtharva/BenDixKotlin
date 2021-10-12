package com.bendix.module.login.viewModel

import com.bendix.base.BaseViewModel
import com.bendix.module.login.model.LoginModel
import com.bendix.module.login.model.SendLoginBody
import com.bendix.module.login.response.LoginResponse
import com.bendix.utility.Constants
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class LoginViewModel : BaseViewModel() {
    lateinit var loginModel: LoginModel

    fun callLogin(
        userName: String,
        password: String,
    ) {
        val sendLoginBody = SendLoginBody(userName, password)

        APIClient.getApi()!!.sendLogin(sendLoginBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<LoginResponse> {
                    override fun status(response: LoginResponse?, message: String) {
                        if (response != null) {
                            loginModel = LoginModel(response)
                            messageEvent.value = Constants.SUCCESS_LOGIN
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }
}