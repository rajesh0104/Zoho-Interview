package com.zoho.interview.userdetails.viewmodel

import android.util.Log
import com.zoho.interview.AppController
import com.zoho.interview.rest.pogo.ApiData
import com.zoho.interview.userdetails.view.UserDetailPageFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailPageModelView(var instanceOfFragment: UserDetailPageFragment) {
    fun getUserWeatherDetails(latLong: String) {
        val weatherApiRequest =
            AppController().restClient(true).restApiServices.getCurrentUserWeatherDetails(
                "5d0fda94b91f49f7b7961010222905",
                latLong
            )
        weatherApiRequest.enqueue(object : Callback<ApiData> {
            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Log.e("failed", "api failed", t)
            }

            override fun onResponse(
                call: Call<ApiData>,
                response: Response<ApiData>
            ) {
                apiResponseWeather(response.body()?.location, response.body()?.current)
            }
        })
    }

    private fun apiResponseWeather(
        results: ApiData.ApiResults.Location?,
        current: ApiData.CurrentWeatherData?
    ) {
        instanceOfFragment.onUserWeatherDetailsUpdated(results, current)
    }
}