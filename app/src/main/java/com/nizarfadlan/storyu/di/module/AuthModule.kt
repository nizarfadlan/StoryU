package com.nizarfadlan.storyu.di.module

import com.nizarfadlan.storyu.data.datasource.AuthDataSource
import com.nizarfadlan.storyu.data.repository.AuthRepositoryImpl
import com.nizarfadlan.storyu.domain.repository.AuthRepository
import org.koin.dsl.module

val authDataSourceModule = module {
    single { AuthDataSource(get(), get()) }
}

val authRepositoryImplModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
}