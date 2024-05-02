package com.vasilyev.kuickhackjvm.network

import retrofit2.Retrofit


object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.100:8080"

    private val _retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
    }

    val apiService: ApiService by lazy {
        _retrofit.create(ApiService::class.java)
    }
}