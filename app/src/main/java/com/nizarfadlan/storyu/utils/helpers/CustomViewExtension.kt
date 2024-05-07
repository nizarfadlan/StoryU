package com.nizarfadlan.storyu.utils.helpers

import android.content.Context

fun Int.dpToPx(context: Context): Float {
    val scale = context.resources.displayMetrics.density
    return this * scale
}