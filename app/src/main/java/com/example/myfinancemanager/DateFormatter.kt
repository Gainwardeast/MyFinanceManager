package com.example.myfinancemanager

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val dayMonthFormat = SimpleDateFormat("d MMMM", Locale("ru"))
private val fullFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
private val shortFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))

/**
 * "Сегодня", "Вчера" или "13 сентября"
 */
fun formatDateHuman(timestamp: Long): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = timestamp }

    val isSameDay =
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val isYesterday =
        yesterday.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)

    return when {
        isSameDay -> "Сегодня"
        isYesterday -> "Вчера"
        now.get(Calendar.YEAR) == date.get(Calendar.YEAR) ->
            dayMonthFormat.format(Date(timestamp))
        else -> fullFormat.format(Date(timestamp))
    }
}

/**
 * "13.09.2026"
 */
fun formatDateShort(timestamp: Long): String =
    shortFormat.format(Date(timestamp))