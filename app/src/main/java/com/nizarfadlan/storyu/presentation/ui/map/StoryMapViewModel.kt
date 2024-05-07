package com.nizarfadlan.storyu.presentation.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.domain.repository.StoryRepository
import com.nizarfadlan.storyu.presentation.common.OperationLiveData
import kotlinx.coroutines.launch

class StoryMapViewModel(repository: StoryRepository) : ViewModel() {
    val recentStories: LiveData<ResultState<List<Story>>> =
        OperationLiveData<ResultState<List<Story>>> {
            viewModelScope.launch {
                repository.getStories(true).collect {
                    postValue(it)
                }
            }
        }
}