package com.bendix.webservice.interfaces

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIMethodCall {
    /*POST*/

    /*POST*/
    @Headers("Accept: application/json")
    @POST("{module}")
    fun doRequestPOST(
        @Path("module") module: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @POST("{module}/{serviceName}")
    fun doRequestPOST(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @POST("{module}/{id}/{serviceName}")
    fun doRequestPOST(
        @Path("module") module: String,
        @Path("id") Id: String,
        @Path("serviceName") serviceName: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @PUT("{module}/{id}/{serviceName}/{serviceId}")
    fun doRequestPOST(
        @Path("module") module: String,
        @Path("id") Id: String,
        @Path("serviceName") serviceName: String,
        @Path("serviceId") serviceId: String,
        @Body data: RequestBody
    ): Call<JsonObject>


    /*GET*/
    @Headers("Accept: application/json")
    @GET("{module}")
    fun doRequestGET(@Path("module") module: String): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("{module}")
    fun doRequestGET(
        @Path("module") module: String,
        @QueryMap params: Map<String, String>
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("{module}/{serviceName}")
    fun doRequestGET(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("{module}/{serviceName}")
    fun doRequestGET(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @QueryMap params: Map<String, String>
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("{module}/{serviceName}/{subserviceName}")
    fun doRequestGET(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @Path("subserviceName") subserviceName: String,
        @QueryMap params: Map<String, String>
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("{module}/{id}/{serviceName}/{subServiceName}")
    fun doRequestGET(
        @Path("module") module: String,
        @Path("id") Id: String,
        @Path("serviceName") serviceName: String,
        @Path("subServiceName") subServiceName: String
    ): Call<JsonObject>

    /*PUT*/

    /*PUT*/
    @Headers("Accept: application/json")
    @PUT("{module}")
    fun doRequestPUT(
        @Path("module") module: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @PUT("{module}/{serviceName}")
    fun doRequestPUT(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @PUT("{module}/{id}/{serviceName}")
    fun doRequestPUT(
        @Path("module") module: String,
        @Path("id") Id: String,
        @Path("serviceName") serviceName: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @PUT("{module}/{id}/{serviceName}/{serviceId}")
    fun doRequestPUT(
        @Path("module") module: String,
        @Path("id") Id: String,
        @Path("serviceName") serviceName: String,
        @Path("serviceId") serviceId: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    /*DELETE*/

    /*DELETE*/
    @Headers("Accept: application/json")
    @HTTP(method = "DELETE", path = "{module}", hasBody = true)
    fun doRequestDELETE(
        @Path("module") module: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @HTTP(method = "DELETE", path = "{module}/{serviceName}", hasBody = true)
    fun doRequestDELETE(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @Body data: RequestBody
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @HTTP(method = "DELETE", path = "{module}/{serviceName}/{id}", hasBody = true)
    fun doRequestDELETE(
        @Path("module") module: String,
        @Path("serviceName") serviceName: String,
        @Path("id") id: String,
        @Body data: RequestBody
    ): Call<JsonObject>

}