package com.nizarfadlan.storyu.utils.helpers

import android.content.Context
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.utils.constant.AppConstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val String.isEmailCorrect: Boolean
    get() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.isFullNameCorrect: Boolean
    get() = !Regex("[^a-zA-Z ]").containsMatchIn(this)

val String.isLengthPasswordCorrect: Boolean
    get() = this.length >= AppConstant.PASSWORD_LENGTH

fun String.capitalizeWords(delimiter: String = " ") =
    split(delimiter).joinToString(delimiter) { word ->
        val smallCaseWord = word.lowercase()
        smallCaseWord.replaceFirstChar(Char::titlecaseChar)
    }

fun String.getTimeAgo(context: Context): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormat.parse(this)
    val now = Date()
    val seconds = ((now.time - (date?.time ?: Date().time)) / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when {
        days > 0 -> context.getString(R.string.label_day, days)
        hours > 0 -> context.getString(R.string.label_hour, hours)
        minutes > 0 -> context.getString(R.string.label_minutes, minutes)
        else -> context.getString(R.string.label_seconds, seconds)
    }
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}