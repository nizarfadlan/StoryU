package com.nizarfadlan.storyu.data.remote.response

import com.google.gson.annotations.SerializedName
import com.nizarfadlan.storyu.domain.model.Story

data class DetailStoryResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: Story
)