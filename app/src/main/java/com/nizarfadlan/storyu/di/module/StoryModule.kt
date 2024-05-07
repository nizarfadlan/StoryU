package com.nizarfadlan.storyu.di.module

import com.nizarfadlan.storyu.data.datasource.StoryDataSource
import com.nizarfadlan.storyu.data.repository.StoryRepositoryImpl
import com.nizarfadlan.storyu.domain.repository.StoryRepository
import org.koin.dsl.module

val storyDataSourceModule = module {
    single { StoryDataSource(get(), get()) }
}

val storyRepositoryImplModule = module {
    factory<StoryRepository> { StoryRepositoryImpl(get()) }
}