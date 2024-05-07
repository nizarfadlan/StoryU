package com.nizarfadlan.storyu.data.datasource

import com.nizarfadlan.storyu.data.pref.UserSessionPreference
import com.nizarfadlan.storyu.data.remote.request.LoginRequest
import com.nizarfadlan.storyu.data.remote.request.RegisterRequest
import com.nizarfadlan.storyu.data.remote.service.AuthService
import com.nizarfadlan.storyu.di.module.preferenceModule
import com.nizarfadlan.storyu.di.module.remoteModule
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.utils.helpers.createResponse
import com.nizarfadlan.storyu.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthDataSource(
    private val service: AuthService,
    private val pref: UserSessionPreference
) {
    suspend fun signUp(
        request: RegisterRequest
    ): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val response = service.register(request)
            if (response.error) {
                emit(ResultState.Error(response.message))
                return@flow
            }

            emit(ResultState.Success(response.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun signIn(
        request: LoginRequest
    ): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            wrapEspressoIdlingResource {
                val response = service.login(request)
                if (response.error) {
                    emit(ResultState.Error(response.message))
                    return@flow
                }

                pref.saveSession(response.loginResult)
                reloadModule()
                emit(ResultState.Success(response.message))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun signOut(): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            pref.logout()
            reloadModule()
            emit(ResultState.Success("Logout success"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }

    fun getSession() = pref.getSession()

    private fun reloadModule() {
        unloadKoinModules(preferenceModule)
        loadKoinModules(preferenceModule)

        unloadKoinModules(remoteModule)
        loadKoinModules(remoteModule)
    }
}