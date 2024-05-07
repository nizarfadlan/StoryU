package com.nizarfadlan.storyu.presentation.ui.main.listStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nizarfadlan.storyu.domain.repository.StoryRepository

class StoryListViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStories() = repository.getPagingStories().cachedIn(viewModelScope)
}