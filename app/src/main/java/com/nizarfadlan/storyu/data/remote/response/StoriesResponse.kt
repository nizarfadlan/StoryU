package com.nizarfadlan.storyu.data.remote.response

import com.google.gson.annotations.SerializedName
import com.nizarfadlan.storyu.domain.model.Story

data class StoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<Story>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
