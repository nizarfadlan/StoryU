package com.nizarfadlan.storyu.data.datasource

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.nizarfadlan.storyu.data.local.room.StoryUDatabase
import com.nizarfadlan.storyu.data.mediator.StoryRemoteMediator
import com.nizarfadlan.storyu.data.remote.service.StoryService
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.utils.constant.AppConstant.DEFAULT_SIZE_STORY
import com.nizarfadlan.storyu.utils.constant.AppConstant.MULTIPART_FILE_NAME
import com.nizarfadlan.storyu.utils.helpers.createResponse
import com.nizarfadlan.storyu.utils.helpers.toMultipart
import com.nizarfadlan.storyu.utils.helpers.toRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class StoryDataSource(
    private val database: StoryUDatabase,
    private val service: StoryService
) {
    suspend fun fetchStories(
        isLocationEnabled: Boolean = false
    ): Flow<ResultState<List<Story>>> = getStories(isLocationEnabled = isLocationEnabled)

    suspend fun fetchStories(
        size: Int?,
        isLocationEnabled: Boolean = false
    ): Flow<ResultState<List<Story>>> =
        getStories(size = size, isLocationEnabled = isLocationEnabled)

    fun fetchPagingStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_SIZE_STORY,
                enablePlaceholders = true
            ),
            remoteMediator = StoryRemoteMediator(database, service),
            pagingSourceFactory = {
                database.getStoryDao().getAllStories()
            }
        ).liveData
    }

    suspend fun fetchStoryDetail(storyId: String): Flow<ResultState<Story>> = flow {
        emit(ResultState.Loading)
        try {
            val story = database.getStoryDao().getStory(storyId)
            if (story != null) {
                emit(ResultState.Success(story))
                return@flow
            }

            val response = service.getStoryDetail(storyId)
            if (response.error) {
                emit(ResultState.Error(response.message))
                return@flow
            }

            emit(ResultState.Success(response.story))
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }

    suspend fun addStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val response = service.addStory(
                photo = photo.toMultipart(MULTIPART_FILE_NAME),
                description = description.toRequestBody(),
                latitude = latitude.toRequestBody(),
                longitude = longitude.toRequestBody(),
            )

            if (response.error) {
                emit(ResultState.Error(response.message))
                return@flow
            }

            emit(ResultState.Success(response.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }

    private suspend fun getStories(
        page: Int? = null,
        size: Int? = null,
        isLocationEnabled: Boolean = false
    ) = flow {
        emit(ResultState.Loading)
        try {
            val response = service.getStories(
                page,
                size,
                location = if (isLocationEnabled) 1 else 0
            )

            if (response.error) {
                emit(ResultState.Error(response.message))
                return@flow
            }

            emit(ResultState.Success(response.listStory))
        } catch (e: Exception) {
            emit(ResultState.Error(e.createResponse()?.message ?: ""))
        }
    }
}