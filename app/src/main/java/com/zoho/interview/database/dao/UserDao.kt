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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetails(userDetails: User?);

    @Query(DatabaseConstant.TABLE_SELECT_FROM_QUERY + DatabaseConstant.TABLE_NAME_USER_DETAILS + " WHERE is_user_active = 1")
    fun getUserDetails(): List<User>?

    @Query("SELECT * FROM USER_DETAILS WHERE is_user_active = 1 LIMIT :limit OFFSET :pageNumber")
    fun getUserDetails(limit: Int, pageNumber: Int): List<User>?

    @Query("SELECT * FROM USER_DETAILS  WHERE is_user_active = 1 AND user_name LIKE '%' || :searchedKey || '%'")
    fun getCurrentUserSearchedDetails(searchedKey: String?): List<User>?

    @Query("DELETE FROM USER_DETAILS")
    fun deleteUserDetailsTable()

    @Query("SELECT * FROM USER_DETAILS WHERE user_id =:userId")
    fun getSelectedUserDetails(userId: String): User
}