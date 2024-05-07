package com.nizarfadlan.storyu.data.remote.service

import com.nizarfadlan.storyu.data.remote.response.BasicResponse
import com.nizarfadlan.storyu.data.remote.response.DetailStoryResponse
import com.nizarfadlan.storyu.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoriesResponse

    @GET("stories/{storyId}")
    suspend fun getStoryDetail(
        @Path("storyId") id: String
    ): DetailStoryResponse

    @POST("stories")
    @Multipart
    suspend fun addStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): BasicResponse
}