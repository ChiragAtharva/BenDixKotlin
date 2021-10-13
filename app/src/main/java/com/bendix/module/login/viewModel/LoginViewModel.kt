package com.bendix.module.login.viewModel

import android.content.Context
import com.bendix.base.BaseViewModel
import com.bendix.module.login.model.LoginModel
import com.bendix.module.login.model.SendLoginBody
import com.bendix.module.login.response.LoginResponse
import com.bendix.utility.Constants
import com.bendix.utility.ProgressDialogUtil
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class LoginViewModel : BaseViewModel() {
    var loginModel = LoginModel()

    fun callLogin(
        mContext: Context,
        userName: String,
        password: String,
    ) {
        val sendLoginBody = SendLoginBody(userName, password)
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        APIClient.getApi()!!.sendLogin(sendLoginBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<LoginResponse> {
                    override fun status(response: LoginResponse?, message: String) {
                        ProgressDialogUtil.getInstance()!!.hideProgressBar(mContext)
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