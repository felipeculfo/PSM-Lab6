// ZoneSelectionState.kt
package com.example.zegarek

const val EXTRA_SELECTED_ZONES = "com.example.lab6.EXTRA_SELECTED_ZONES"

val AVAILABLE_ZONES: List<String> = listOf(
    "Europe/Warsaw",
    "America/New_York",
    "Asia/Tokyo",
    "Australia/Sydney",
    "America/Sao_Paulo",
    "Africa/Cairo"
)

fun toggleZoneSelection(current: List<String>, zoneId: String): List<String> =
    if (zoneId in current) current - zoneId else current + zoneId