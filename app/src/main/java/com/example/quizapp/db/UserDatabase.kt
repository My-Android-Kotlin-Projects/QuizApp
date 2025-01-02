package com.example.quizapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "user_db"
    }
    abstract fun userDao(): UserDao
}