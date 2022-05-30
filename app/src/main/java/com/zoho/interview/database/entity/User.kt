package com.zoho.interview.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zoho.interview.database.DatabaseConstant

@Entity(tableName = DatabaseConstant.TABLE_NAME_USER_DETAILS)
data class User(
    @PrimaryKey()
    @ColumnInfo(name = "user_id")
    var userId: String,

    @ColumnInfo(name = "gender")
    var gender: String?,

    @ColumnInfo(name = "user_name")
    var userName: String?,

    @ColumnInfo(name = "user_age")
    var age: Int?,

    @ColumnInfo(name = "user_mail_id")
    var mailId: String?,

    @ColumnInfo(name = "user_mobile_number")
    var mobileNumber: String?,

    @ColumnInfo(name = "postal_code")
    var postalCode: String?,

    @ColumnInfo(name = "user_profile_picture_medium")
    var medium: String?,

    @ColumnInfo(name = "user_profile_picture_large")
    var large: String?,

    @ColumnInfo(name = "user_state")
    var state: String?,

    @ColumnInfo(name = "user_country")
    var country: String?,

    @ColumnInfo(name = "user_coordination_latitude")
    var latitude: String?,

    @ColumnInfo(name = "user_coordination_longitude")
    var longitude: String?,

    @ColumnInfo(name = "is_user_active")
    var userActiveState: Int?
)