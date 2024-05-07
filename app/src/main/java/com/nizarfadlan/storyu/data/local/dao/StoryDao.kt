package com.nizarfadlan.storyu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nizarfadlan.storyu.domain.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(users: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("SELECT * FROM story WHERE id = :id")
    fun getStory(id: String): Story

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}