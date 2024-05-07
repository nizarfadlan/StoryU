package com.nizarfadlan.storyu.presentation.ui.main.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.repository.StoryRepository
import com.nizarfadlan.storyu.presentation.common.OperationLiveData
import kotlinx.coroutines.launch
import java.io.File

class StoryAddViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(
        photo: File,
        description: String,
        latitude: Double?,
        longitude: Double?
    ): LiveData<ResultState<String>> = OperationLiveData<ResultState<String>> {
        viewModelScope.launch {
            repository.addStory(photo, description, latitude, longitude).collect { postValue(it) }
        }
    }
}