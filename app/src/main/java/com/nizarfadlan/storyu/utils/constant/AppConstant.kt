package com.nizarfadlan.storyu.utils.constant

import android.Manifest

object AppConstant {
    const val PREFS_SETTINGS = "setting.pref"
    const val PREFS_SESSIONS = "session.pref"
    const val MULTIPART_FILE_NAME = "photo"
    const val MULTIPART_FORM_DATA = "multipart/form-data"
    const val MAXIMAL_SIZE = 1000000
    const val PASSWORD_LENGTH = 8
    const val DEFAULT_SIZE_STORY = 10
    const val DB_NAME = "storyu-db"

    const val REQUIRED_CAMERA_PERMISSION = Manifest.permission.CAMERA
    val REQUIRED_LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}