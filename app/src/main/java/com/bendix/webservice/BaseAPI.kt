package com.bendix.webservice

import android.content.Context
import com.bendix.R
import com.bendix.utility.Common
import com.bendix.utility.LogUtil
import com.bendix.utility.ProgressDialogUtil
import com.bendix.webservice.interfaces.APIKeys
import com.bendix.webservice.interfaces.ResponseListener
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class BaseAPI {
    private val TAG = javaClass.simpleName
    private var mContext: Context? = null
    private var responseListener: ResponseListener? = null
    private var url = ""
    private var response = ""

    constructor() {}
    constructor(mContext: Context, responseListener: ResponseListener?) {
        this.mContext = mContext
        this.responseListener = responseListener
    }

    fun apiResponse(apiRequest: Call<JsonObject>) {
        apiRequest.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                try {
                    ProgressDialogUtil.getInstance()!!.hideProgressBar(mContext)
                    if (response != null) {
                        LogUtil.displayLog(TAG, "API: onResponse Code : " + response.code())
                        LogUtil.displayLog(TAG, "API: onResponse: $response")
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                this@BaseAPI.response = response.body().toString() //Response
                                if (call != null) url = "" + call.request().url //Url
                                LogUtil.displayLog(
                                    TAG,
                                    "API: onResponse - Body: " + this@BaseAPI.response
                                )
                                LogUtil.displayLog(TAG, "API: url: $url")
                                responseListener!!.onSuccess(this@BaseAPI.response)
                            } else responseListener!!.onFailure("Null body response")
                        } else {
                            val errorMessage =
                                Objects.requireNonNull(response.errorBody())!!.string()
                            LogUtil.displayLogError(TAG, errorMessage)
                            responseListener!!.onSuccess(errorMessage)
                        }
                    } else responseListener!!.onFailure("Null response")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ProgressDialogUtil.getInstance()!!.hideProgressBar(mContext)
                    responseListener!!.onFailure(e.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                ProgressDialogUtil.getInstance()!!.hideProgressBar(mContext)
                LogUtil.displayLogError(
                    TAG,
                    "API: onFailure: call - $call\nThrowable - $t"
                )
                url = "" + call.request().url //Url
                LogUtil.displayLog(TAG, "API: url: $url")

                //Check - error
                if (Objects.requireNonNull(t.message)!!
                        .contains(APIKeys.ApiErrors.API_ERROR_NO_ADDRESS_ASSOCIATED_WITH_HOSTNAME)
                    || t.message!!.contains(APIKeys.ApiErrors.API_ERROR_UNABLE_TO_RESOLVE_HOST)
                ) {
                    Common.showAlert(
                        mContext!!,
                        mContext!!.getString(R.string.app_name),
                        mContext!!.getString(R.string.check_internet_connection)
                    )
                } else if (t.toString().contains(APIKeys.ApiErrors.API_ERROR_FAILED_TO_CONNECT)) {
                    responseListener!!.onFailure(mContext!!.getString(R.string.check_internet_connection))
                } else if (t.toString().contains(APIKeys.ApiErrors.API_ERROR_TIMEOUT)) {
                    responseListener!!.onFailure(mContext!!.getString(R.string.check_internet_connection))
                } else {
                    responseListener!!.onFailure(t.toString())
                }
            }
        })
    }
}
