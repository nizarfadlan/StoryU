package com.nizarfadlan.storyu.app

import android.app.Application
import com.nizarfadlan.storyu.BuildConfig
import com.nizarfadlan.storyu.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class StoryU : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@StoryU)
            modules(appModule)
        }
    }
}