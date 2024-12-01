package com.example.cv2

import android.content.Context
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("user/create.php")
    suspend fun registerUser(@Body userInfo: RegistrationRequest): Response<RegistrationResponse>

    @POST("user/login.php")
    suspend fun loginUser(@Body userInfo: LoginRequest): Response<LoginResponse>

    @GET("user/get.php")
    suspend fun getUser(
        @Query("id") id: String
    ): Response<UserResponse>

    @POST("user/refresh.php")
    suspend fun refreshToken(
        @Body refreshInfo: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @POST("user/refresh.php")
    fun refreshTokenBlocking(
        @Body refreshInfo: RefreshTokenRequest
    ): Call<RefreshTokenResponse>

    @GET("geofence/list.php")
    suspend fun listGeofence(): Response<GeofenceResponse>

    @POST("geofence/update.php")
    suspend fun updateGeofence(
        @Body geofenceInfo: GeofenceRequest
    ): Response<GeofenceUpdateResponse>

    @DELETE("geofence/update.php")
    suspend fun removeGeofence(): Response<GeofenceUpdateResponse>

    @Multipart
    @POST("https://upload.mcomputing.eu/photo.php")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<User>

    @DELETE("https://upload.mcomputing.eu/photo.php")
    suspend fun deleteImage(): Response<User>

    companion object{
        fun create(context: Context): ApiService {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuthenticator(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://zadanie.mpage.sk/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

data class RegistrationRequest(val name: String, val email: String, val password: String)
data class RegistrationResponse(val uid: String, val access: String, val refresh: String)

data class RefreshTokenRequest(val refresh: String)
data class RefreshTokenResponse(val uid: String, val access: String, val refresh: String)

data class LoginRequest(val name: String, val password: String)
data class LoginResponse(val uid: String, val access: String, val refresh: String)

data class UserResponse(val id: String, val name: String, val photo: String)

data class GeofenceResponse(val me: GeofenceMeResponse, val list: List<GeofenceUserResponse>)
data class GeofenceMeResponse (val uid: String, val lat: Double, val lon: Double, val radius: Double)
data class GeofenceUserResponse (val uid: String, val radius: Double, val updated: String, val name: String, val photo: String)

data class GeofenceRequest(val lat: Double, val lon: Double, val radius: Double)
data class GeofenceUpdateResponse(val success: Boolean)