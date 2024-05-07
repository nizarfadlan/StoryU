package com.nizarfadlan.storyu.presentation.widget

import android.content.Context
import androidx.room.Room
import com.nizarfadlan.storyu.BuildConfig
import com.nizarfadlan.storyu.data.datasource.StoryDataSource
import com.nizarfadlan.storyu.data.local.room.StoryUDatabase
import com.nizarfadlan.storyu.data.pref.UserSessionPreference
import com.nizarfadlan.storyu.data.remote.HeaderInterceptor
import com.nizarfadlan.storyu.data.remote.service.StoryService
import com.nizarfadlan.storyu.utils.constant.AppConstant.DB_NAME
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RecentStoriesProvider(
    private val context: Context
) {
    private val provideDataSource: StoryDataSource =
        StoryDataSource(provideDatabase(), provideApiService(createRetrofit()))

    suspend fun getRecentStories() = provideDataSource.fetchStories(size = SIZE_STORY)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createClient())
            .build()
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(preferences))
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun provideDatabase(): StoryUDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryUDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val preferences = UserSessionPreference(context)

    private fun provideApiService(retrofit: Retrofit): StoryService =
        retrofit.create(StoryService::class.java)

    companion object {
        const val SIZE_STORY = 4
        fun getInstance(context: Context): RecentStoriesProvider {
            return RecentStoriesProvider(context)
        }
    }
}