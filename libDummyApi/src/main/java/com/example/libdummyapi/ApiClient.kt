package com.example.libdummyapi

import com.example.libdummyapi.api.DummyService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val APP_ID = "61553dbbd1f3f624401558b6"

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader("app-id", APP_ID)
                    .build()
                it.proceed(request)
            }
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dummyapi.io/data/v1/")
            .build()
    }

    val api : DummyService by lazy {
        retrofit.create(DummyService::class.java)
    }
}