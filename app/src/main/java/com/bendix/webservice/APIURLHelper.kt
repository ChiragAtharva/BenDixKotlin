package com.bendix.webservice

object APIURLHelper {
    fun updateBaseURL() {
        APIClient.retrofit = null
        APIConstants.BASE_URL = APIConstants.URL + APIConstants.API + APIConstants.URL_VERSION + "/"
    }
}