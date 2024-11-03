package com.example.cv2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _userResult = MutableLiveData<Pair<String,User?>>()
    val userResult: LiveData<Pair<String, User?>> get() = _userResult

    fun registerUser(username: String, email: String, password: String, repeatPassword: String) {
        viewModelScope.launch {
            _userResult.postValue(dataRepository.apiRegisterUser(username, email, password, repeatPassword))
        }
    }

    fun loginUser(nameOrEmail: String, password: String) {
        viewModelScope.launch {
            _userResult.postValue(dataRepository.apiLoginUser(nameOrEmail, password))
        }
    }
}
