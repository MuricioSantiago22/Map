package com.example.map.data.repository

import com.example.map.data.model.LoginResponse
import com.example.map.data.remote.UserDataSource

class UserRepoImpl(private val dataSource: UserDataSource) : UserRepo{
    override suspend fun signIn(email: String, password: String): LoginResponse = dataSource.signIn()

}