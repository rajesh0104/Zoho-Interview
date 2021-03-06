package com.zoho.interview.rest

import com.zoho.interview.rest.pogo.ApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiServices {
    @GET("api")
    fun getUserDetails(
        @Query("results", encoded = true) dataSize: Int,
        @Query("page", encoded = true) pageNumber: Int
    ): Call<ApiData>

    @GET("v1/current.json")
    fun getCurrentUserWeatherDetails(
        @Query("key", encoded = true) apiKey: String,
        @Query("q", encoded = true) latLong: String
    ): Call<ApiData>
}