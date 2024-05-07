package com.nizarfadlan.storyu.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {
    suspend fun getStories(isLocationEnabled: Boolean): Flow<ResultState<List<Story>>>
    fun getPagingStories(): LiveData<PagingData<Story>>
    suspend fun getStoryDetail(storyId: String): Flow<ResultState<Story>>
    suspend fun addStory(photo: File, description: String, latitude: Double?, longitude: Double?): Flow<ResultState<String>>
}