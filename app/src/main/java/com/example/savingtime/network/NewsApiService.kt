package com.example.savingtime.network

import com.example.savingtime.model.EkonomiApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://alan-api.adaptivenetlab.site/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NewsApiService {
    @GET("ekonomi")
    suspend fun getNews(): EkonomiApiResponse
}

object NewsApi {
    val service: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }