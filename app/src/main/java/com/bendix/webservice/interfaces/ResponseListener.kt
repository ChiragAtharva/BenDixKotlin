package com.bendix.webservice.interfaces

interface ResponseListener {
    fun onSuccess(response : String)
    fun onFailure(error: String)
}