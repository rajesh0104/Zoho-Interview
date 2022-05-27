package com.zoho.interview.dashboard.modelview

import android.util.Log
import com.zoho.interview.AppController
import com.zoho.interview.rest.pogo.ApiData
import com.zoho.interview.dashboard.view.DashBoardActivityView
import com.zoho.interview.database.DatabaseHelper
import com.zoho.interview.database.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardModelView(var dashboardActivityInstance: DashBoardActivityView) {

    fun getUserDetails(pageSize: Int) {
        val readUserDataApiRequest =
            AppController().restClient?.restApiServices?.getUserDetails(pageSize)
        readUserDataApiRequest?.enqueue(object : Callback<ApiData> {
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

    private fun onUserDetailsApiSuccess(apiResults: List<ApiData.ApiResults>?) {

        val userList: ArrayList<User> = ArrayList()
        apiResults?.indices?.forEach { i ->

            val apiResultData = apiResults[i]
            val userDetails: User = User(
                apiResultData.login?.username.toString(),
                apiResultData.name?.title,
                apiResultData.name?.first,
                apiResultData.name?.last,
                apiResultData.dob?.age,
                apiResultData.email,
                apiResultData.phone,
                apiResultData.picture?.medium,
                apiResultData.picture?.large,
                apiResultData.location?.state,
                apiResultData.location?.country,
                apiResultData.location?.coordinates?.latitude,
                apiResultData.location?.coordinates?.longitude,
            )
            userList.add(userDetails)
        }
        DatabaseHelper().storeUserDetails(dashboardActivityInstance, userList)
        dashboardActivityInstance.onUserDetailsUpdated()
    }
}