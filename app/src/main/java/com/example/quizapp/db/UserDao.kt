package com.example.quizapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM UserModel")
    fun getAllUsers(): LiveData<List<UserModel>>

    @Insert
    fun insertUser(user: UserModel)

    @Query("DELETE FROM UserModel")
    fun deleteAllUsers()

}