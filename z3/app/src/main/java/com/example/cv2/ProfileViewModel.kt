package com.example.cv2

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _profileResult = MutableLiveData<String>()
    val profileResult: LiveData<String> get() = _profileResult

    private val _userResult = MutableLiveData<User?>()
    val userResult: LiveData<User?> get() = _userResult

    val sharingLocation = MutableLiveData<Boolean?>(null)

    fun loadUser(uid: String) {
        viewModelScope.launch {
            val result = dataRepository.apiGetUser(uid)
            _profileResult.postValue(result.first ?: "")
            _userResult.postValue(result.second)
        }
    }

    fun getPhotoUrl(): String {
        val prefix = "https://upload.mcomputing.eu/"
        return if (!userResult.value?.photo.isNullOrEmpty()) {
            prefix + userResult.value?.photo
        } else {
            ""
        }
    }

    fun uploadPhoto(photoUri: Uri) {
        viewModelScope.launch {
            val result = dataRepository.apiUploadPhoto(photoUri)
            _profileResult.postValue(result.first)
            _userResult.postValue(result.second)
        }
    }

    fun deletePhoto() {
        viewModelScope.launch {
            val result = dataRepository.apiDeletePhoto()
            _profileResult.postValue(result.first)
            _userResult.postValue(result.second)
        }
    }
}