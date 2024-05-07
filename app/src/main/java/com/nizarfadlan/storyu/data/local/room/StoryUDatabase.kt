package com.nizarfadlan.storyu.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nizarfadlan.storyu.data.local.dao.RemoteKeysDao
import com.nizarfadlan.storyu.data.local.dao.StoryDao
import com.nizarfadlan.storyu.domain.model.RemoteKeys
import com.nizarfadlan.storyu.domain.model.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryUDatabase : RoomDatabase() {
    abstract fun getStoryDao(): StoryDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}