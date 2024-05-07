package com.nizarfadlan.storyu.utils.helpers

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.presentation.ui.map.adapter.CustomInfoWindow
import com.nizarfadlan.storyu.presentation.ui.map.adapter.SnippetWindowData
import timber.log.Timber

fun GoogleMap.boundsCameraToMarkers(locations: List<LatLng>, context: Context) {
    val boundsBuilder = LatLngBounds.builder()
    for (location in locations) {
        boundsBuilder.include(location)
    }
    val bounds = boundsBuilder.build()

    val zoomLevel = 100
    val cuf = CameraUpdateFactory.newLatLngBounds(
        bounds,
        context.resources.displayMetrics.widthPixels,
        context.resources.displayMetrics.heightPixels,
        zoomLevel
    )

    this.animateCamera(cuf, 1000, null)
}

fun GoogleMap.setCustomStyle(context: Context) {
    try {
        val success =
            this.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        if (!success) {
            Timber.e("Style parsing failed.")
        }
    } catch (exception: Resources.NotFoundException) {
        Timber.e("Can't find style. Error: ", exception)
    }
}

fun List<Story>.convertToLatLng(): List<LatLng> {
    val listMarker = ArrayList<LatLng>()
    for (story in this) {
        if (story.latitude != null && story.longitude != null) {
            listMarker.add(LatLng(story.latitude.toDouble(), story.longitude.toDouble()))
        }
    }
    return listMarker
}

fun GoogleMap.addMultipleMarker(stories: List<Story>, context: Context) {
    for (story in stories) {
        if (story.latitude != null && story.longitude != null) {
            val marker = this.addMarker(
                createMarkerOptions(
                    LatLng(story.latitude.toDouble(), story.longitude.toDouble()),
                    story.name,
                    story.description.substring(0, story.description.length.coerceAtMost(50)),
                    story.photoUrl
                )
            )
            this.setInfoWindowAdapter(CustomInfoWindow(context))
            marker?.tag = story
        }
    }
}

private fun createMarkerOptions(
    location: LatLng,
    markerName: String,
    markerDescription: String,
    markerPhoto: String
): MarkerOptions {
    val iconDrawable = R.drawable.map_pin
    val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
    val snippet = Gson().toJson(SnippetWindowData(markerDescription, markerPhoto))

    return MarkerOptions().position(location)
        .title(markerName)
        .snippet(snippet)
        .icon(icon)
}