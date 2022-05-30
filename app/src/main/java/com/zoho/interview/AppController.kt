package com.zoho.interview

import android.app.Application
import com.zoho.interview.rest.RestClient

class AppController : Application() {


    var instance: AppController? = null
        get() {
            if (field == null) {
                this.instance = this
            }
            return field
        }
        private set

    var restClient: RestClient? = null


    override fun onCreate() {
        super.onCreate()
    }

    fun restClient(isLocationService: Boolean): RestClient {
        this.restClient = RestClient(isLocationService)
        return restClient as RestClient
    }
}