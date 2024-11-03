package com.example.cv2

import com.google.gson.Gson
import java.io.IOException

data class User(
    val id: String,
    val access: String,
    val refresh: String
) {
    fun toJson(): String? {
        return try {
            Gson().toJson(this)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    companion object {
        fun fromJson(string: String): User? {
            return try {
                Gson().fromJson(string, User::class.java)
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
        }
    }
}
