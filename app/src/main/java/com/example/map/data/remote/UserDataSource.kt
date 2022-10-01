package com.example.map.data.remote
import com.example.map.application.AppConstants

import com.example.map.data.model.UserResponse
import com.example.map.data.repository.WebService


class UserDataSource(private val webService: WebService){
    suspend fun signIn(): UserResponse = webService.signIn(email = String(), password = String())
}
