package com.kotlinmvvmstructure.genericResponse

open class BaseResponse {
    var error: String? = null
    var success: Boolean = false
    var token: String = ""
    var message: String? = null
}