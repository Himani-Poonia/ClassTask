package com.example.classtask

object Util {
    fun getTimeAgo(time: Long): String {
        var time = time
        val SECOND_MILLIS = 1000 //milliseconds in a second
        val MINUTE_MILLIS = 60 * SECOND_MILLIS //milliseconds in a minute
        val HOUR_MILLIS = 60 * MINUTE_MILLIS //milliseconds in an hour
        val DAY_MILLIS = 24 * HOUR_MILLIS //milliseconds in a day
        val MONTH_MILLIS = 30 * DAY_MILLIS //milliseconds in a month
        val YEAR_MILLIS = 12 * MONTH_MILLIS //milliseconds in a year
        time *= 1000 //convert time to milliseconds

        //current time
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return ""
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 59 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 2 * HOUR_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 2 * DAY_MILLIS) {
            "yesterday"
        } else if (diff < 30 * DAY_MILLIS) {
            (diff / DAY_MILLIS).toString() + " days ago"
        } else if (diff < 2 * MONTH_MILLIS) {
            "a month ago"
        } else if (diff < YEAR_MILLIS) {
            (diff / MONTH_MILLIS).toString() + " months ago"
        } else if (diff < 2 * YEAR_MILLIS) {
            "a year ago"
        } else (diff / YEAR_MILLIS).toString() + " years ago"
    }
}