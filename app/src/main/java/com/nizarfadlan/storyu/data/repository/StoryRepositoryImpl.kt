package com.nizarfadlan.storyu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.nizarfadlan.storyu.data.datasource.StoryDataSource
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.domain.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.io.File


class StoryRepositoryImpl(
    private val dataSource: StoryDataSource
): StoryRepository {
    override suspend fun getStories(
        isLocationEnabled: Boolean
    ): Flow<ResultState<List<Story>>> = dataSource.fetchStories(isLocationEnabled).flowOn(
        Dispatchers.IO
    )

    override fun getPagingStories(): LiveData<PagingData<Story>> =
        dataSource.fetchPagingStories()

    override suspend fun getStoryDetail(storyId: String): Flow<ResultState<Story>> =
        dataSource.fetchStoryDetail(storyId).flowOn(Dispatchers.IO)

    override suspend fun addStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<ResultState<String>> =
        dataSource.addStory(photo, description, latitude, longitude).flowOn(
            Dispatchers.IO
        )
}