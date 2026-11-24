// WorldClockFormatter.kt
package com.example.zegarek

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class ZoneClockItem(
    val zoneId: String,
    val currentTime: String,
    val diffFromLocal: String
)

data class WorldClockUiState(
    val localTime: String,
    val localDate: String,
    val zones: List<ZoneClockItem>
)

fun buildWorldClockUiState(
    now: Instant,
    selectedZoneIds: List<String>,
    zoneToIanaMap: Map<String, String> = emptyMap(),
    localZone: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): WorldClockUiState {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", locale)
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.FULL)
        .withLocale(locale)
        .withZone(localZone)

    val localZoned = now.atZone(localZone)
    val localOffsetSeconds = localZoned.offset.totalSeconds

    val items = selectedZoneIds
        .mapNotNull { rawId ->
            val trimmed = rawId.trim()
            val mappedId = zoneToIanaMap[trimmed] ?: trimmed.replace(' ', '_')
            val zone = runCatching { ZoneId.of(mappedId) }.getOrNull() ?: return@mapNotNull null
            rawId to zone
        }
        .map { (displayLabel, zone) ->
            val zoned = now.atZone(zone)
            val diffHours = (zoned.offset.totalSeconds - localOffsetSeconds) / 3600.0
            ZoneClockItem(
                zoneId = displayLabel,
                currentTime = zoned.format(timeFormatter),
                diffFromLocal = formatHourDiff(diffHours)
            )
        }

    return WorldClockUiState(
        localTime = localZoned.format(timeFormatter),
        localDate = localZoned.format(dateFormatter),
        zones = items
    )
}

private fun formatHourDiff(diffHours: Double): String {
    val formattedNumber = if (diffHours % 1.0 == 0.0) {
        diffHours.toInt().toString()
    } else {
        diffHours.toString()
    }
    return when {
        diffHours > 0.0 -> "+$formattedNumber h"
        else -> "$formattedNumber h"
    }
}