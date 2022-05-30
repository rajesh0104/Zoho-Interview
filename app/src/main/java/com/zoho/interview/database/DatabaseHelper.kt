package com.zoho.interview.database

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.zoho.interview.database.entity.User

class DatabaseHelper {

    fun storeUserDetails(activity: Activity, userDetail: List<User>?) {
        AppDatabase.getInstance(activity).userDao().insertUserDetails(userDetail)
    }

    fun storeUserDetails(activity: Activity, userDetail: User?) {
        AppDatabase.getInstance(activity).userDao().insertUserDetails(userDetail)
    }

    fun getCurrentUserDetails(activity: Activity): List<User>? {
        return AppDatabase.getInstance(activity).userDao().getUserDetails()
    }

    fun getCurrentUserSearchedDetails(activity: Activity, searchedKey: String?): List<User>? {
        return AppDatabase.getInstance(activity).userDao()
            .getCurrentUserSearchedDetails(searchedKey)
    }

    fun deleteUserDetails(activity: Activity) {
        AppDatabase.getInstance(activity).userDao()
            .deleteUserDetailsTable()
    }

    fun getSelectedUserDetails(activity: Activity, userId: String): User {
        return AppDatabase.getInstance(activity).userDao()
            .getSelectedUserDetails(userId)
    }
}