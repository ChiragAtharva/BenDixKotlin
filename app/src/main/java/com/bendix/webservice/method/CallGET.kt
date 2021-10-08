package com.bendix.webservice.method

import android.content.Context
import com.bendix.utility.Common
import com.bendix.utility.ProgressDialogUtil
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseAPI
import com.bendix.webservice.interfaces.APIMethodCall
import com.bendix.webservice.interfaces.ResponseListener
import com.google.gson.JsonObject
import retrofit2.Call
import java.lang.Exception
import java.util.HashMap

class CallGET {
    private lateinit var apiMethodCall: APIMethodCall
    private var hasProgressBar = true

    constructor() {
        apiMethodCall = APIClient.getClient()!!.create(APIMethodCall::class.java)
    }

    constructor(hasProgressBar: Boolean) {
        apiMethodCall = APIClient.getClient()!!.create(APIMethodCall::class.java)
        this.hasProgressBar = hasProgressBar
    }

    //Get - Module/....
    operator fun get(mContext: Context?, module: String?, responseListener: ResponseListener?) {
        try {
            if (hasProgressBar) ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
            val jsonObjectCall = apiMethodCall.doRequestGET(module!!)
            if (jsonObjectCall != null) BaseAPI(mContext!!, responseListener).apiResponse(
                jsonObjectCall
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Common.unableToProcessRequest(mContext!!, e.toString())
        }
    }

    //Get - Module/QueryString/....
    operator fun get(
        mContext: Context?,
        module: String,
        queryMap: HashMap<String, String>,
        responseListener: ResponseListener?
    ) {
        try {
            if (hasProgressBar) ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
            val jsonObjectCall: Call<JsonObject> = apiMethodCall.doRequestGET(module, queryMap)
            if (jsonObjectCall != null) BaseAPI(mContext!!, responseListener).apiResponse(
                jsonObjectCall
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Common.unableToProcessRequest(mContext!!, e.toString())
        }
    }

    //Get - Module/Service/QueryString/....
    operator fun get(
        mContext: Context?,
        module: String?,
        service: String?,
        queryMap: HashMap<String, String>,
        responseListener: ResponseListener?
    ) {
        try {
            if (hasProgressBar) ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
            val jsonObjectCall = apiMethodCall.doRequestGET(
                module!!,
                service!!, queryMap
            )
            if (jsonObjectCall != null) BaseAPI(mContext!!, responseListener).apiResponse(
                jsonObjectCall
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Common.unableToProcessRequest(mContext!!, e.toString())
        }
    }

    //Get - Module/Service/SubService/QueryString/....
    operator fun get(
        mContext: Context?,
        module: String,
        service: String,
        subService: String,
        queryMap: HashMap<String, String>,
        responseListener: ResponseListener?
    ) {
        try {
            if (hasProgressBar) ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
            val jsonObjectCall: Call<JsonObject> =
                apiMethodCall.doRequestGET(module, service, subService, queryMap)
            if (jsonObjectCall != null) BaseAPI(mContext!!, responseListener).apiResponse(
                jsonObjectCall
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Common.unableToProcessRequest(mContext!!, e.toString())
        }
    }
}