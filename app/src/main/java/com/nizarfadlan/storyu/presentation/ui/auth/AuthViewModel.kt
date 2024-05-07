package com.nizarfadlan.storyu.presentation.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.User
import com.nizarfadlan.storyu.domain.repository.AuthRepository
import com.nizarfadlan.storyu.presentation.common.OperationLiveData
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
    ): LiveData<ResultState<String>> {
        return OperationLiveData<ResultState<String>> {
            viewModelScope.launch {
                repository.signIn(email, password)
                    .collect {
                        postValue(it)
                    }
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
    ): LiveData<ResultState<String>> {
        return OperationLiveData<ResultState<String>> {
            viewModelScope.launch {
                repository.signUp(name, email, password)
                    .collect {
                        postValue(it)
                    }
            }
        }
    }

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    val signOut: LiveData<ResultState<String>> = OperationLiveData<ResultState<String>> {
        viewModelScope.launch {
            repository.signOut()
                .collect {
                    postValue(it)
                }
        }
    }

}