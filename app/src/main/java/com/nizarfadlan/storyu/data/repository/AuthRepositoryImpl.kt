package com.nizarfadlan.storyu.data.repository

import com.nizarfadlan.storyu.data.datasource.AuthDataSource
import com.nizarfadlan.storyu.data.remote.request.LoginRequest
import com.nizarfadlan.storyu.data.remote.request.RegisterRequest
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.User
import com.nizarfadlan.storyu.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val dataSource: AuthDataSource
): AuthRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<ResultState<String>> {
        val requestData = RegisterRequest(name, email, password)
        return dataSource.signUp(requestData).flowOn(Dispatchers.IO)
    }

    override suspend fun signIn(email: String, password: String): Flow<ResultState<String>> {
        val requestData = LoginRequest(email, password)
        return dataSource.signIn(requestData).flowOn(Dispatchers.IO)
    }

    override fun getSession(): Flow<User> = dataSource.getSession().flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<ResultState<String>> {
        return dataSource.signOut().flowOn(Dispatchers.IO)
    }
}