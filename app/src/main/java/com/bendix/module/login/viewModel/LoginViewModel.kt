package com.bendix.module.login.viewModel

import android.content.Context
import com.bendix.R
import com.bendix.base.BaseViewModel
import com.bendix.module.login.model.LoginModel
import com.bendix.module.login.model.SendLoginBody
import com.bendix.module.login.response.LoginResponse
import com.bendix.utility.Common
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback
import com.bendix.webservice.interfaces.APICallListener

class LoginViewModel : BaseViewModel() {
    lateinit var loginModel: LoginModel
    fun callLogin(
        mContext: Context,
        userName: String,
        password: String,
        apiCallListener: APICallListener
    ) {
        val sendLoginBody = SendLoginBody(userName, password)

        APIClient.getApi()!!.sendLogin(sendLoginBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<LoginResponse> {
                    override fun status(response: LoginResponse?, message: String) {
                        if (response != null) {
                            loginModel = LoginModel(response)
                            if (Common.isJSONValid(response.toString()))
                                apiCallListener.getResponse(response.toString())
                            else Common.showAlert(
                                mContext,
                                mContext.getString(R.string.error),
                                mContext.getString(R.string.invalid_json_response)
                            )
                            //messageEvent.value = ApplicationConstants.SUCCESS_SEND_OTP
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }
}