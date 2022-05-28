package com.zoho.interview.database

import android.app.Activity
import com.zoho.interview.database.entity.User

class DatabaseHelper {

    fun storeUserDetails(activity: Activity, userDetail: List<User>?) {
        AppDatabase.getInstance(activity).userDao().insertUserDetails(userDetail)
    }

    fun getCurrentUserDetails(activity: Activity): List<User>? {
        return AppDatabase.getInstance(activity).userDao().getUserDetails()
    }

    fun getCurrentUserSearchedDetails(activity: Activity,searchedKey:String?): List<User>? {
        return AppDatabase.getInstance(activity).userDao().getCurrentUserSearchedDetails(searchedKey)
    }
}