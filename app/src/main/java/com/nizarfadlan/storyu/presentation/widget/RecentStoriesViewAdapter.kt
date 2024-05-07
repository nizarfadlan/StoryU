package com.nizarfadlan.storyu.presentation.widget

import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.presentation.widget.RecentStoriesWidget.Companion.EXTRA_ITEM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException

class RecentStoriesViewAdapter(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {
    private val stories = mutableListOf<Story>()
    private val storyProvider = RecentStoriesProvider.getInstance(context)

    override fun onCreate() {
        stories.clear()
    }

    override fun onDataSetChanged() {
        val identifyToken: Long = Binder.clearCallingIdentity()
        CoroutineScope(Dispatchers.IO).launch {
            storyProvider.getRecentStories().collect { result ->
                if (result is ResultState.Success) {
                    stories.clear()
                    stories.addAll(result.data)
                }
            }
        }
        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun onDestroy() {
        stories.clear()
    }

    override fun getCount(): Int = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_recent_story)
        if (stories.isNotEmpty()) {
            val data = stories[position]

            remoteViews.setTextViewText(R.id.tv_item_name, data.name)
            try {
                val bitmap = Glide.with(context).asBitmap().load(data.photoUrl).submit().get()
                remoteViews.setImageViewBitmap(R.id.iv_item_photo, bitmap)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val extras = Bundle()
            extras.putString(EXTRA_ITEM, data.id)
        }

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_layout_loading)
    }

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    companion object {
        fun getInstance(context: Context): RecentStoriesViewAdapter {
            return RecentStoriesViewAdapter(context)
        }
    }
}