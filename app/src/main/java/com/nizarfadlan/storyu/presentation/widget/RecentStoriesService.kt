package com.nizarfadlan.storyu.presentation.widget

import android.content.Intent
import android.widget.RemoteViewsService

class RecentStoriesService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RecentStoriesViewAdapter.getInstance(this)
    }
}