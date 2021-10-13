package com.bendix.webservice

import com.bendix.BenDexApplication
import com.bendix.R
import com.bendix.utility.Common.showAlert
import com.bendix.utility.Common.showAlertWithLogout
import com.bendix.utility.Constants
import com.bendix.utility.LogUtil
import com.bendix.webservice.interfaces.APIKeys
import com.kotlinmvvmstructure.genericResponse.BaseResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

open class BaseCallback<T>(private val listener: OnCallback<T>) : Callback<T> {
    private val TAG = javaClass.simpleName
    override fun onResponse(call: Call<T>, response: Response<T>) {
        when {
            response.isSuccessful -> {
                when {
                    response.body() is ResponseBody -> listener.status(response.body(), "")
                    (response.body() as BaseResponse).success -> listener.status(
                        response.body(),
                        ""
                    )

                    else -> {
                        val errorMessage = Objects.requireNonNull(response.errorBody())
                            ?.string()
                        LogUtil.displayLogError(TAG, errorMessage!!)
                        listener.status(null, errorMessage.toString())
                        showAlert(
                            BenDexApplication.contextApp,
                            BenDexApplication.contextApp.getString(R.string.error),
                            errorMessage.toString()
                        )
                    }
                }
            }
            (response.code() == Constants.TOKEN_EXPIRED_CODE) -> {
                val errorMessage1 = Objects.requireNonNull(response.errorBody())
                    ?.string()
                if (errorMessage1!!.contains(APIConstants.ERROR_TOKEN_EXPIRED)) {
                    showAlertWithLogout(
                        BenDexApplication.contextApp,
                        BenDexApplication.contextApp.getString(R.string.error),
                        errorMessage1
                    )
                } else {
                    var message: String = errorMessage1
                    try {
                        val responseObject = JSONObject(errorMessage1)
                        message = responseObject.optString(APIKeys.ApiResponse.MESSAGE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        showAlert(
                            BenDexApplication.contextApp,
                            BenDexApplication.contextApp.getString(R.string.error),
                            message
                        )
                    }
                }
                listener.status(null, Constants.TOKEN_EXPIRED_CODE.toString())
            }
            (response.code() in 502..503) -> {
                listener.status(
                    null,
                    Objects.requireNonNull(response.errorBody())
                    !!.string()
                )
                showAlert(
                    BenDexApplication.contextApp,
                    BenDexApplication.contextApp.getString(R.string.app_name),
                    Objects.requireNonNull(response.errorBody())
                    !!.string()
                )
            }

            else -> {
                listener.status(null, BenDexApplication.contextApp.getString(R.string.try_again))
                showAlert(
                    BenDexApplication.contextApp,
                    BenDexApplication.contextApp.getString(R.string.app_name),
                    BenDexApplication.contextApp.getString(R.string.try_again)
                )
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        val errorMessage = if (t is IOException) {
            BenDexApplication.contextApp.getString(R.string.check_internet_connection)
        } else {
            BenDexApplication.contextApp.getString(R.string.something_went_wrong)
        }


        LogUtil.displayLogError(
            TAG,
            "API: onFailure: call - $call\nThrowable - $t"
        )

        val url = "" + call.request().url //Url

        LogUtil.displayLog(TAG, "API: url: $url")

        //Check - error
        if (Objects.requireNonNull(t.message)
            !!.contains(APIKeys.ApiErrors.API_ERROR_NO_ADDRESS_ASSOCIATED_WITH_HOSTNAME)
            || t.message!!.contains(APIKeys.ApiErrors.API_ERROR_UNABLE_TO_RESOLVE_HOST)
        ) {
            showAlert(
                BenDexApplication.contextApp,
                BenDexApplication.contextApp.getString(R.string.app_name),
                BenDexApplication.contextApp.getString(R.string.check_internet_connection)
            )
        } else if (t.toString().contains(APIKeys.ApiErrors.API_ERROR_FAILED_TO_CONNECT)) {
            showAlert(
                BenDexApplication.contextApp,
                BenDexApplication.contextApp.getString(R.string.app_name),
                BenDexApplication.contextApp.getString(R.string.check_internet_connection)
            )
        } else if (t.toString().contains(APIKeys.ApiErrors.API_ERROR_TIMEOUT)) {
            showAlert(
                BenDexApplication.contextApp,
                BenDexApplication.contextApp.getString(R.string.app_name),
                BenDexApplication.contextApp.getString(R.string.check_internet_connection)
            )
        } else {
            listener.status(null, errorMessage)
            showAlert(
                BenDexApplication.contextApp,
                BenDexApplication.contextApp.getString(R.string.app_name),
                errorMessage
            )
        }
    }

    interface OnCallback<T> {
        fun status(response: T?, message: String)
    }
}
