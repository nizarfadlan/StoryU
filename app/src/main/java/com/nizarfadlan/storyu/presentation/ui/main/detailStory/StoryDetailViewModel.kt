package com.nizarfadlan.storyu.presentation.ui.main.detailStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.domain.repository.StoryRepository
import com.nizarfadlan.storyu.presentation.common.OperationLiveData
import kotlinx.coroutines.launch

class StoryDetailViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStory(storyId: String): LiveData<ResultState<Story>> =
        OperationLiveData<ResultState<Story>> {
            viewModelScope.launch { repository.getStoryDetail(storyId).collect { postValue(it) } }
        }
}