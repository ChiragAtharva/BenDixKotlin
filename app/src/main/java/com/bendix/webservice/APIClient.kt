package com.bendix.webservice

import com.bendix.webservice.interfaces.ApiInterface
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    var retrofit: Retrofit? = null
    private var api: ApiInterface? = null

    fun getApi(): ApiInterface? {

        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (api == null) {

            retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .client(mClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            api = retrofit!!.create(ApiInterface::class.java)
        }
        return api
    }

    private fun mClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                /*if (response.code() in 401..403) {
                    Log.e("LOGOUT INTERCEPTER: ", "force logout")

                    val baseResponse = Gson().fromJson(response.body()!!.string(), BaseResponse::class.java)

                    ApplicationClass.activeActivity!!.runOnUiThread {
                        AlertDialog.Builder(ApplicationClass.activeActivity!!)
                            .setTitle("")
                            .setMessage(baseResponse.error)
                            .setCancelable(false)
                            .setPositiveButton("ok") { _: DialogInterface, i: Int ->
                                //ApplicationClass.activeActivity!!.finishAffinity()
                                PrefManager.instance.token = ""
                                ApplicationClass.activeActivity!!.startActivity(ApplicationClass.activeActivity!!.landingPageIntent()
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                            }.show()
                    }
                }else if(response.code()==409){
                    AppUpdater.instance.open()
                }*/
                response
            }
            .addInterceptor(interceptor)
            /*.addNetworkInterceptor { chain ->
                lateinit var request: Request
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("X-Authorization", PrefManager.instance.token)

                request = requestBuilder.build()
                return@addNetworkInterceptor chain.proceed(request)
            }*/
            .build()
    }
}