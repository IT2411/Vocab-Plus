package com.vocabplus.app.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val ISO_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun todayIso(): String = LocalDate.now().format(ISO_FORMATTER)

    fun formatIsoToReadable(dateIso: String): String {
        return try {
            val date = LocalDate.parse(dateIso, ISO_FORMATTER)
            date.format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
        } catch (_: Exception) {
            dateIso
        }
    }
}