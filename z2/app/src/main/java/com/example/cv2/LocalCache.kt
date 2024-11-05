package com.example.cv2

import androidx.lifecycle.LiveData

class LocalCache(private val dao: DataAccessObject) {

    suspend fun logoutUser() {
        deleteUserItems()
    }

    suspend fun insertUserItems(items: List<UserEntity>) {
        dao.insertUserItems(items)
    }

    fun getUserItem(uid: String): LiveData<UserEntity?> {
        return dao.getUserItem(uid)
    }

    fun getUsers(): LiveData<List<UserEntity?>> = dao.getUsers()

    suspend fun deleteUserItems() {
        dao.deleteUserItems()
    }
}