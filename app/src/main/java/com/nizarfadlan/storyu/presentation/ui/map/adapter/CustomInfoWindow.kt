package com.nizarfadlan.storyu.presentation.ui.map.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.nizarfadlan.storyu.R

data class SnippetWindowData(
    val description: String,
    val imageUrl: String
)

class CustomInfoWindow(private val context: Context) : InfoWindowAdapter {
    private val images: HashMap<Marker, Bitmap> = HashMap()
    private val targets: HashMap<Marker, CustomTarget<Bitmap>> = HashMap()

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = View.inflate(context, R.layout.layout_info_window, null)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvDescription = view.findViewById<TextView>(R.id.tv_description)
        val ivPhoto = view.findViewById<ShapeableImageView>(R.id.iv_photo)

        val dataSnippet: SnippetWindowData =
            Gson().fromJson(marker.snippet, SnippetWindowData::class.java)
        tvTitle.text = marker.title
        tvDescription.text = dataSnippet.description

        val image = images[marker]
        if (image != null) {
            ivPhoto.setImageBitmap(image)
        } else {
            Glide.with(context)
                .asBitmap()
                .load(dataSnippet.imageUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(12)))
                .into(getTarget(marker))
        }

        return view
    }

    inner class InfoTarget(private var marker: Marker) : CustomTarget<Bitmap>() {
        override fun onLoadCleared(placeholder: Drawable?) {
            images.remove(marker)
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            images[marker] = resource
            marker.showInfoWindow()
        }
    }

    private fun getTarget(marker: Marker): CustomTarget<Bitmap> {
        var target = targets[marker]
        if (target == null) {
            target = InfoTarget(marker)
            targets[marker] = target
        }
        return target
    }
}