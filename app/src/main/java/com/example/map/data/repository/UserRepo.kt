package com.example.map.data.repository


import com.example.map.data.model.UserResponse

interface UserRepo{
     suspend fun signIn(email:String, password:String): UserResponse
}