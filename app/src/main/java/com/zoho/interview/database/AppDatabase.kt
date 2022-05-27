package com.zoho.interview.database

import android.app.Activity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zoho.interview.AppController
import com.zoho.interview.database.dao.UserDao
import com.zoho.interview.database.entity.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private var appDataBaseInstance: AppDatabase? = null

        @Synchronized
        fun getInstance(activity: Activity): AppDatabase {
            if (appDataBaseInstance == null) {
                appDataBaseInstance = Room.databaseBuilder(
                    activity,
                    AppDatabase::class.java,
                    DatabaseConstant.DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return appDataBaseInstance!!
        }
    }
}