package com.example.map.data.repository

import com.example.map.application.AppConstants
import com.example.map.data.model.UserResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST



interface WebService {

    @POST("login")
    suspend fun signIn(@Body email:String, password:String): UserResponse
}

object RetrofitClient{
    val webService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)

    }
}

