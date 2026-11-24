// ZoneSelectionState.kt
package com.example.zegarek

const val EXTRA_SELECTED_ZONES = "com.example.zegarek.EXTRA_SELECTED_ZONES"

fun toggleZoneSelection(current: List<String>, zoneId: String): List<String> =
    if (zoneId in current) current - zoneId else current + zoneId
