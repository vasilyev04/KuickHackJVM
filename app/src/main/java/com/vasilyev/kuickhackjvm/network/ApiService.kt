package com.vasilyev.kuickhackjvm.network

import com.vasilyev.kuickhackjvm.network.dto.CheckResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("/gpt/chat2")
    suspend fun checkIDCard(
        @Query("type") type: String = "IDCard",
        @Part file: MultipartBody.Part
    ): Response<CheckResponse>
}