package com.zoho.interview.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient(isLocation: Boolean) {

    public var restApiServices: RestApiServices

    init {
        val client = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        val gson: Gson = GsonBuilder().serializeNulls().setLenient().create()

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(interceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val restAdapter = Retrofit.Builder()
            .baseUrl(
                if (isLocation) {
                    "https://api.weatherapi.com/"
                } else {
                    "https://randomuser.me/"
                }
            )
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        restApiServices = restAdapter.create(RestApiServices::class.java)
    }
}