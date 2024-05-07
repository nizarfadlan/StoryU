package com.nizarfadlan.storyu.utils.helpers

import com.google.gson.Gson
import com.nizarfadlan.storyu.data.remote.response.BasicResponse
import retrofit2.HttpException

fun Exception.createResponse(): BasicResponse? {
    return when (this) {
        is HttpException -> {
            Gson().fromJson(response()?.errorBody()?.string(), BasicResponse::class.java)
        }

        else -> {
            BasicResponse(
                message = this.message ?: "Error",
                error = true
            )
        }
    }
}