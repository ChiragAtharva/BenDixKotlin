package com.bendix.webservice.interfaces

interface APIKeys {
    //API - Known Error
    interface ApiErrors {
        companion object {
            const val API_ERROR_UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
            const val API_ERROR_NO_ADDRESS_ASSOCIATED_WITH_HOSTNAME =
                "No address associated with hostname"
            const val API_ERROR_FAILED_TO_CONNECT = "java.net.ConnectException: Failed to connect"
            const val API_ERROR_TIMEOUT = "failed to connect"
        }
    }

    interface Headers {
        companion object {
            const val HEADER_ACCESS_TOKEN = "access-token"
            const val HEADER_CLIENT = "client"
            const val HEADER_TOKEN_TYPE = "token-type"
            const val HEADER_UID = "uid"
        }
    }

    interface ApiResponse {
        companion object {
            const val TYPE = "type"
            const val RESPONSE_DATA = "data"
            const val ATTRIBUTES = "attributes"
            const val ERROR = "error"
            const val MESSAGE = "message"
            const val SUCCESS = "success"
            const val ERRORS = "errors"
            const val RESPONSE_OBJECT = "meta"
            const val RESPONSE_MESSAGE = "msg"
            const val ERROR_MESSAGE = "full_messages"
        }
    }

    interface ValidateToken {
        interface Request {
            companion object {
                const val USER_ID = "uid"
                const val TOKEN = "token"
            }
        }

        interface Response {
            companion object {
                const val TOKEN = "Token"
                const val EXPIRED = "Expired"
            }
        }
    }

    interface Login {
        interface Request {
            companion object {
                const val DB = "db"
                const val DB_VERSION = "erpwave_12.0"
                const val LOGIN = "login"
                const val PASSWORD = "password"
            }
        }

        interface Response {
            companion object {
                const val USER_ID = "uid"
                const val COMPANY_ID = "company_id"
                const val PARTNER_ID = "partner_id"
                const val ACCESS_TOKEN = "access_token"
            }
        }
    }

    interface Customer {
        interface Request {
            companion object {
                const val TOKEN = "token"
                const val BARCODE = "barcode"
            }
        }

        interface Response {
            companion object {
                const val CUSTOMER_ID = "customer_id"
                const val NAME = "name"
                const val PRICE_LIST = "pricelist"
                const val ORDER_ID = "order_id"
            }
        }
    }

    interface SalesOrder {
        interface Request {
            companion object {
                const val TOKEN = "token"
                const val BARCODE = "barcode"
                const val ORDER_ID = "order_id"
                const val LINE_ID = "line_id"
                const val QUANTITY = "qty"
            }
        }
    }
}