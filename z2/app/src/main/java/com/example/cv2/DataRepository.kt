package com.example.cv2

import java.io.IOException
import java.security.MessageDigest

class DataRepository private constructor(
    private val service: ApiService
){
    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null
        private val lock = Any()

        fun getInstance(): DataRepository =
            INSTANCE ?: synchronized(lock) {
                INSTANCE
                    ?: DataRepository(ApiService.create()).also { INSTANCE = it }
            }
    }

    suspend fun apiRegisterUser(username: String, email: String, password: String, repeatPassword: String) : Pair<String,User?>{
        if (username.isEmpty()) return Pair("Username cannot be empty", null)
        if (email.isEmpty()) return Pair("E-mail cannot be empty", null)
        if (password.isEmpty()) return Pair("Password cannot be empty", null)
        if (repeatPassword.isEmpty()) return Pair("Please confirm your password", null)
        if (password != repeatPassword) return Pair("Passwords do not match", null)

        val hashedPassword = hashPassword(password)
        try {
            val response = service.registerUser(UserRegistration(username, email, hashedPassword))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    if (jsonResponse.uid == "-1") return Pair("Username already exists", null)
                    if (jsonResponse.uid == "-2") return Pair("E-mail already exists", null)

                    return Pair("", User(jsonResponse.uid, jsonResponse.access, jsonResponse.refresh))
                }
            }
            return Pair("Failed to create user", null)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to create user.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to create user.", null)
    }

    suspend fun apiLoginUser(nameOrEmail: String, password: String): Pair<String, User?> {
        if (nameOrEmail.isEmpty()) return Pair("Username or E-mail cannot be empty", null)
        if (password.isEmpty()) return Pair("Password cannot be empty", null)

        val hashedPassword = hashPassword(password)
        return try {
            val response = service.loginUser(UserLogin(nameOrEmail, hashedPassword))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    if (jsonResponse.uid == "-1") return Pair("Username or password is incorrect", null)

                    Pair("", User(jsonResponse.uid, jsonResponse.access, jsonResponse.refresh))
                } ?: Pair("Failed to login", null)
            } else {
                Pair("Failed to login", null)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            Pair("Check internet connection. Failed to login.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Pair("Fatal error. Failed to login.", null)
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
