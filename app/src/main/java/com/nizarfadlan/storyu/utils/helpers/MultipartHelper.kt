package com.nizarfadlan.storyu.utils.helpers

import com.nizarfadlan.storyu.utils.constant.AppConstant.MULTIPART_FORM_DATA
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun File.toMultipart(name: String): MultipartBody.Part {
    return MultipartBody.Part
        .createFormData(
            name,
            filename = this.name,
            body = this.asRequestBody()
        )
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}

fun Double?.toRequestBody(): RequestBody? {
    if (this == null) return null
    return this.toString().toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}