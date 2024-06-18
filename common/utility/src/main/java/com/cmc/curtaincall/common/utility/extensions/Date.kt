package com.cmc.curtaincall.common.utility.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
private const val DEFAULT_UI_DATE_PATTERN = "yyyy.MM.dd"
private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
private const val SIMPLE_DATE_PATTERN = "yy.MM.dd(E)"
private const val PARTY_DATE_PATTERN = "yyyy.MM.dd(E)"
private const val PARTY_TIME_PATTERN = "HH:mm"
fun getTodayDate(): String {
    return SimpleDateFormat(DATE_PATTERN, Locale.KOREA).format(Calendar.getInstance().time)
}

fun String.toDateWithDay(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
    return SimpleDateFormat("yyyy.MM.dd(E)").format(date)
}

fun String.toDateInKorea(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
    return SimpleDateFormat("yyyy.MM.dd(E) a hh:mm").format(date)
}

fun String?.toTimePM(): String {
    if (this == null) return ""
    if (isEmpty()) return ""
    val date = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US).apply {
        setTimeZone(TimeZone.getTimeZone("GMT"))
    }.parse(this)
    return SimpleDateFormat("a h:mm", Locale.KOREA).format(date)
}

fun String.toSimpleDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}

fun String.toTime(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
    return SimpleDateFormat("HH:mm").format(date)
}

fun String.toChangeDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd").parse(this)
    return SimpleDateFormat("yy.MM.dd").format(date)
}

fun String.toChangeFullDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy-MM-dd").parse(this)
    return SimpleDateFormat("yyyy.MM.dd").format(date)
}

fun String.toChangeServerDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat("yyyy.MM.dd").parse(this)
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}

fun changeShowAt(date: String, time: String): String {
    val showDate = SimpleDateFormat("yyyy. MM. dd").parse(date)
    val changeDate = SimpleDateFormat("yyyy-MM-dd").format(showDate)
    return "${changeDate}T$time:00"
}

// ///
fun String.convertDDay(): Long {
    if (isEmpty()) return 0L
    val date = SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return (today.time.time - date.time) / (60 * 60 * 24 * 1000L)
    } ?: kotlin.run {
        return 0L
    }
}

fun String.convertSimpleDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        return SimpleDateFormat(SIMPLE_DATE_PATTERN, Locale.KOREA).format(date)
    } ?: kotlin.run {
        return ""
    }
}

fun String.convertDefaultDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat(DEFAULT_DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        return SimpleDateFormat(DEFAULT_UI_DATE_PATTERN, Locale.KOREA).format(date)
    } ?: kotlin.run {
        return ""
    }
}

fun String.convertUIDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat(DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        return SimpleDateFormat(DEFAULT_UI_DATE_PATTERN, Locale.KOREA).format(date)
    } ?: kotlin.run {
        return ""
    }
}

fun String.convertPartyDate(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat(DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        return SimpleDateFormat(PARTY_DATE_PATTERN, Locale.KOREA).format(date)
    } ?: kotlin.run {
        return ""
    }
}

fun String.convertPartyTime(): String {
    if (isEmpty()) return ""
    val date = SimpleDateFormat(DATE_PATTERN, Locale.KOREA).parse(this)
    date?.let { date ->
        return SimpleDateFormat(PARTY_TIME_PATTERN, Locale.KOREA).format(date)
    } ?: kotlin.run {
        return ""
    }
}

fun Date?.toPM(): String {
    val dateFormat = SimpleDateFormat("a h:mm", Locale.getDefault())
    return this?.let { dateFormat.format(it) } ?: ""
}

fun Date.toSeparatorDate(): String {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd(E)", Locale.getDefault())
    return dateFormat.format(this)
}
