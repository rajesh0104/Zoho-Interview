package com.zoho.interview.dashboard.modelview

import android.util.Log
import com.zoho.interview.AppController
import com.zoho.interview.rest.pogo.ApiData
import com.zoho.interview.dashboard.view.UserListingFragment
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.database.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardModelView(var dashboardActivityInstance: UserListingFragment) {

    fun getUserDetails(itemSize: Int, pageNumber: Int) {
        val readUserDataApiRequest =
            AppController().restClient(false).restApiServices.getUserDetails(itemSize, pageNumber)
        readUserDataApiRequest.enqueue(object : Callback<ApiData> {
            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Log.e("failed", "api failed", t)
            }

            override fun onResponse(
                call: Call<ApiData>,
                response: Response<ApiData>
            ) {
                onUserDetailsApiSuccess(response.body()?.results)
            }
        })
    }


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

    private fun onUserDetailsApiSuccess(apiResults: List<ApiData.ApiResults>?) {

        val userList: ArrayList<User> = ArrayList()
        apiResults?.indices?.forEach { i ->

            val apiResultData = apiResults[i]
            val userDetails: User = User(
                apiResultData.login?.username.toString(),
                apiResultData.gender,
                apiResultData.name?.first + " " + apiResultData.name?.last,
                apiResultData.dob?.age,
                apiResultData.email,
                apiResultData.phone,
                apiResultData.location?.postcode.toString(),
                apiResultData.picture?.medium,
                apiResultData.picture?.large,
                apiResultData.location?.state,
                apiResultData.location?.country,
                apiResultData.location?.coordinates?.longitude,
                apiResultData.location?.coordinates?.latitude, 1
            )
            userList.add(userDetails)
        }
        dashboardActivityInstance.activity?.let { DatabaseHelper().storeUserDetails(it, userList) }
        dashboardActivityInstance.onUserDetailsUpdated()
    }

    private fun apiResponseWeather(
        results: ApiData.ApiResults.Location?,
        current: ApiData.CurrentWeatherData?
    ) {
        dashboardActivityInstance.onUserWeatherDetailsUpdated(results, current)
    }
}