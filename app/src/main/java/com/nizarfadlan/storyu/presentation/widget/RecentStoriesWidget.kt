package com.nizarfadlan.storyu.presentation.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.presentation.ui.main.detailStory.StoryDetailFragment.Companion.EXTRA_STORY_ID

class RecentStoriesWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == ITEM_CLICK_ACTION) {
                val viewIndex = intent.getStringExtra(EXTRA_ITEM)

                val pendingIntent: PendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.main_navigation)
                    .setDestination(R.id.storyDetailFragment)
                    .setArguments(bundleOf(EXTRA_STORY_ID to viewIndex))
                    .createPendingIntent()

                pendingIntent.send()
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.recent_stories_widget)

        val widgetAdapterIntent = Intent(context, RecentStoriesService::class.java)
        widgetAdapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        widgetAdapterIntent.data = Uri.parse(widgetAdapterIntent.toUri(Intent.URI_INTENT_SCHEME))

        views.setEmptyView(R.id.list, R.id.emptyText)
        views.setRemoteAdapter(R.id.list, widgetAdapterIntent)

        val clickIntent = Intent(context, RecentStoriesWidget::class.java)
        clickIntent.action = ITEM_CLICK_ACTION
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, clickIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else 0
        )
        views.setPendingIntentTemplate(R.id.list, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        const val EXTRA_ITEM = "${AppWidgetManager.EXTRA_APPWIDGET_ID}.EXTRA_ITEM"
        const val ITEM_CLICK_ACTION = "ITEM_CLICK_ACTION"
    }
}