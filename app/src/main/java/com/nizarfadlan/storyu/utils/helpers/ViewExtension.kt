package com.nizarfadlan.storyu.utils.helpers

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.nizarfadlan.storyu.presentation.ui.base.component.snackbar.CustomSnackbar

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    callback: (CustomSnackbar.() -> Unit)? = null
) {
    val snackbar = CustomSnackbar.make(
        this,
        message,
        duration
    )

    callback?.invoke(snackbar)

    snackbar.show()
}

fun ShapeableImageView.setImageUrl(url: String?) {
    Glide.with(this.rootView).load(url).apply(RequestOptions())
        .into(this)
}

fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }

        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    return fallback
}