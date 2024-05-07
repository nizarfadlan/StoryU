package com.nizarfadlan.storyu.data.remote.service

import com.nizarfadlan.storyu.data.remote.request.LoginRequest
import com.nizarfadlan.storyu.data.remote.request.RegisterRequest
import com.nizarfadlan.storyu.data.remote.response.BasicResponse
import com.nizarfadlan.storyu.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BasicResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}