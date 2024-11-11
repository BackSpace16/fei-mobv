package com.example.cv2

import android.content.Context
import java.io.IOException
import java.security.MessageDigest

class DataRepository private constructor(
    private val service: ApiService,
    private val cache: LocalCache
) {
    companion object {
        const val TAG = "DataRepository"

        @Volatile
        private var INSTANCE: DataRepository? = null
        private val lock = Any()

        fun getInstance(context: Context): DataRepository =
            INSTANCE ?: synchronized(lock) {
                INSTANCE
                    ?: DataRepository(ApiService.create(context),
                       LocalCache(AppRoomDatabase.getInstance(context).appDao())
                    ).also { INSTANCE = it }
            }
    }

    suspend fun apiRegisterUser(email: String, username: String, password: String, repeatPassword: String) : Pair<String,User?>{
        if (username.isEmpty()) return Pair("Username cannot be empty", null)
        if (email.isEmpty()) return Pair("E-mail cannot be empty", null)
        if (password.isEmpty()) return Pair("Password cannot be empty", null)
        if (repeatPassword.isEmpty()) return Pair("Please confirm your password", null)
        if (password != repeatPassword) return Pair("Passwords do not match", null)

        val hashedPassword = hashPassword(password)
        try {
            val response = service.registerUser(RegistrationRequest(username, email, hashedPassword))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    if (jsonResponse.uid == "-1") return Pair("Username already exists", null)
                    if (jsonResponse.uid == "-2") return Pair("E-mail already exists", null)

                    return Pair("", User(username, email, jsonResponse.uid, jsonResponse.access, jsonResponse.refresh))
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

    suspend fun apiLoginUser(username: String, password: String): Pair<String, User?> {
        if (username.isEmpty()) return Pair("Username or E-mail cannot be empty", null)
        if (password.isEmpty()) return Pair("Password cannot be empty", null)

        val hashedPassword = hashPassword(password)
        return try {
            val response = service.loginUser(LoginRequest(username, hashedPassword))
            if (response.isSuccessful) {
                response.body()?.let { jsonResponse ->
                    if (jsonResponse.uid == "-1") return Pair("Username or password is incorrect", null)

                    Pair("", User(username, "", jsonResponse.uid, jsonResponse.access, jsonResponse.refresh))
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

    suspend fun apiGetUser(
        uid: String
    ): Pair<String, User?> {
        try {
            val response = service.getUser(uid)

            if (response.isSuccessful) {
                response.body()?.let {
                    val user = User(it.name, "", it.id, "", "", it.photo)
                    cache.insertUserItems(
                        listOf(
                            UserEntity(
                                user.id, user.username, "", 0.0, 0.0, 0.0, ""
                            )
                        )
                    )
                    return Pair("", user)
                }
            }

            return Pair("Failed to load user", null)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Pair("Check internet connection. Failed to load user.", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Pair("Fatal error. Failed to load user.", null)
    }

    suspend fun apiListGeofence(): String {
        try {
            val response = service.listGeofence()

            if (response.isSuccessful) {
                response.body()?.let { resp ->
                    val users = resp.list.map {
                        UserEntity(
                            it.uid, it.name, it.updated,
                            resp.me.lat, resp.me.lon, it.radius, it.photo
                        )
                    }
                    cache.insertUserItems(users)
                    return ""
                }
            }

            return "Failed to load users"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return "Check internet connection. Failed to load users."
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "Fatal error. Failed to load users."
    }

    suspend fun apiUpdateGeofence(lat: Double, lon: Double, radius: Double): String {
        try {
            val geofenceRequest = GeofenceRequest(lat, lon, radius)
            val response = service.updateGeofence(geofenceRequest)

            return if (response.isSuccessful && response.body()?.success == true) {
                ""
            } else {
                "Failed to update geofence"
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return "Check internet connection. Failed to update geofence."
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "Fatal error. Failed to update geofence."
        }
    }

    suspend fun apiRemoveGeofence(): String {
        try {
            val response = service.removeGeofence()

            return if (response.isSuccessful && response.body()?.success == true) {
                ""
            } else {
                "Failed to remove geofence"
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return "Check internet connection. Failed to remove geofence."
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "Fatal error. Failed to remove geofence."
        }
    }

    fun getUsers() = cache.getUsers()

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
