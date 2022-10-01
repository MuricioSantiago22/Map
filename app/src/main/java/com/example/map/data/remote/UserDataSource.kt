package com.example.map.data.remote
import com.example.map.data.model.LoginResponse

import com.example.map.data.repository.WebService



class UserDataSource(private val webService: WebService){
    suspend fun signIn(): LoginResponse = webService.signIn(email = String(), password = String())
}
