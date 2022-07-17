package com.saifurrahman.gist.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        private val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        fun getDesiredDateFormat(date: String, format: String): String {
            val currentFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val newFormat = SimpleDateFormat(format, Locale.US)
            currentFormat.parse(date)?.let {
                return newFormat.format(it)
            }
            return ""
        }

        fun convertDateToTimestamp(date: String): Long? {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(date)?.let { outputDate ->
                return Timestamp(outputDate.time).time
            }
            return null
        }
    }
}