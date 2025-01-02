package com.example.quizapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val  username: String,
)