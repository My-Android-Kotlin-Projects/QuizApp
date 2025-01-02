package com.example.quizapp

import android.app.Application
import androidx.room.Room
import com.example.quizapp.db.UserDatabase

class MainApplication : Application() {
    companion object {
        lateinit var userdb: UserDatabase
    }
    override fun onCreate() {
        super.onCreate()
        userdb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        ).build()
    }
}