package com.example.dummyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao() : UserDao

    companion object {
        private var INSTANCE : UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_db"
                ).build().also { INSTANCE = it }
            }
        }
    }

}