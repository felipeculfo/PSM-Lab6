// ZoneSelectionState.kt
package com.example.zegarek

fun toggleZoneSelection(current: List<String>, zoneId: String): List<String> =
    if (zoneId in current) current - zoneId else current + zoneId