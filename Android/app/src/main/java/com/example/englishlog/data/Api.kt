package com.example.englishlog.data

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null,
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: User,
)

@Serializable
data class User(
    val id: Long,
    val email: String,
    val name: String? = null,
)

@Serializable
data class Folder(
    val id: Long = 0,
    val name: String,
)

@Serializable
data class Entry(
    val id: Long = 0,
    val folderId: Long,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val createdAt: String? = null,
)

@Serializable
data class Skill(
    val id: String,
    val name: String,
    val file: String,
    val why: String,
)

interface EnglishLogApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @GET("api/folders")
    suspend fun folders(): List<Folder>

    @POST("api/folders")
    suspend fun createFolder(@Body folder: Folder): Folder

    @GET("api/entries")
    suspend fun entries(): List<Entry>

    @POST("api/entries")
    suspend fun createEntry(@Body entry: Entry): Entry

    @GET("api/skills")
    suspend fun skills(): List<Skill>
}

object ApiFactory {
    // Android emulator reaches the host machine at 10.0.2.2.
    // On a physical device, replace this with your computer's LAN IP.
    const val DEFAULT_BASE_URL = "http://10.0.2.2:5050/"

    fun create(baseUrl: String = DEFAULT_BASE_URL): EnglishLogApi {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                },
            )
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(EnglishLogApi::class.java)
    }
}
