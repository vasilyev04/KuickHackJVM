package com.vasilyev.kuickhackjvm.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("http://192.168.0.100:8080")
    suspend fun checkIDCard(
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}