package com.nizarfadlan.storyu.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("userId")
    val id: String,
    val name: String,
    val token: String
)