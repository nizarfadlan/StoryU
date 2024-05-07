package com.nizarfadlan.storyu.domain.repository

import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Flow<ResultState<String>>
    suspend fun signIn(email: String, password: String): Flow<ResultState<String>>
    fun getSession(): Flow<User>
    suspend fun signOut(): Flow<ResultState<String>>
}