package com.example.quizapp.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.MainActivity
import com.example.quizapp.MainApplication
import com.example.quizapp.db.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val userDao  = MainApplication.userdb.userDao()
    val user: LiveData<List<UserModel>> get() = userDao.getAllUsers()

    fun insertUser(username: String) {
       viewModelScope.launch(Dispatchers.IO) {
           userDao.insertUser(UserModel(username = username))
       }
    }
    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteAllUsers()
        }
    }
}