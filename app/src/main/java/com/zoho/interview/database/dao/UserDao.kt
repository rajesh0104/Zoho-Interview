package com.zoho.interview.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zoho.interview.database.DatabaseConstant
import com.zoho.interview.database.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetails(userDetails: List<User>?);

    @Query(DatabaseConstant.TABLE_SELECT_FROM_QUERY + DatabaseConstant.TABLE_NAME_USER_DETAILS)
    fun getUserDetails(): List<User>?
}