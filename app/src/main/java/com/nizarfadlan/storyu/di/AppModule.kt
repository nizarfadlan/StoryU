package com.nizarfadlan.storyu.di

import com.nizarfadlan.storyu.di.module.authDataSourceModule
import com.nizarfadlan.storyu.di.module.authRepositoryImplModule
import com.nizarfadlan.storyu.di.module.localModule
import com.nizarfadlan.storyu.di.module.preferenceModule
import com.nizarfadlan.storyu.di.module.remoteModule
import com.nizarfadlan.storyu.di.module.storyDataSourceModule
import com.nizarfadlan.storyu.di.module.storyRepositoryImplModule
import com.nizarfadlan.storyu.di.module.viewModelModule

val appModule = listOf(
    remoteModule,
    localModule,
    preferenceModule,
    authDataSourceModule,
    authRepositoryImplModule,
    storyDataSourceModule,
    storyRepositoryImplModule,
    viewModelModule
)