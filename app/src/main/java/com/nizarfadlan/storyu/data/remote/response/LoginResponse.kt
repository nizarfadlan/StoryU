package com.nizarfadlan.storyu.data.remote.response

import com.google.gson.annotations.SerializedName
import com.nizarfadlan.storyu.domain.model.User

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: User,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)