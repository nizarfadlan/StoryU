package com.nizarfadlan.storyu.di.module

import android.app.Application
import androidx.room.Room
import com.nizarfadlan.storyu.data.local.room.StoryUDatabase
import com.nizarfadlan.storyu.utils.constant.AppConstant.DB_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

fun provideDatabase(application: Application): StoryUDatabase {
    return Room.databaseBuilder(application, StoryUDatabase::class.java, DB_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

fun provideStoryDao(database: StoryUDatabase) = database.getStoryDao()

val localModule = module {
    single { provideStoryDao(get()) }
    single { provideDatabase(androidApplication()) }
}