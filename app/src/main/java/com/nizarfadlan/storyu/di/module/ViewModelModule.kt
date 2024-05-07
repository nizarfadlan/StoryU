package com.nizarfadlan.storyu.di.module

import com.nizarfadlan.storyu.presentation.ui.auth.AuthViewModel
import com.nizarfadlan.storyu.presentation.ui.main.addStory.StoryAddViewModel
import com.nizarfadlan.storyu.presentation.ui.main.detailStory.StoryDetailViewModel
import com.nizarfadlan.storyu.presentation.ui.main.listStory.StoryListViewModel
import com.nizarfadlan.storyu.presentation.ui.main.setting.SettingViewModel
import com.nizarfadlan.storyu.presentation.ui.map.StoryMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { StoryListViewModel(get()) }
    viewModel { StoryDetailViewModel(get()) }
    viewModel { StoryAddViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { StoryMapViewModel(get()) }
}