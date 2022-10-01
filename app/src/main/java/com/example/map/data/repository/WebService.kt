package com.example.map.data.repository

import com.example.map.application.AppConstants
import com.example.map.data.model.LoginResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface WebService {
    @FormUrlEncoded
    @POST("login/")
    suspend fun signIn(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse
}

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)

            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)

    }
}

