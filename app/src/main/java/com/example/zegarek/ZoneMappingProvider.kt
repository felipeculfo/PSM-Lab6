// ZoneMappingProvider.kt
package com.example.zegarek

import android.content.Context
import java.time.ZoneId

/** Etykieta strefy (zależna od języka) -> identyfikator IANA, np. "Europa: Warszawa" -> "Europe/Warsaw". */
fun loadZoneToIanaMap(context: Context): Map<String, String> {
    val labels = context.resources.getStringArray(R.array.available_zones).toList()
    val ianaIds = context.resources.getStringArray(R.array.available_zone_iana_ids).toList()

    return labels
        .zip(ianaIds)
        .associate { (label, ianaId) -> label.trim() to ianaId.trim() }
}

fun resolveZoneIdFromResources(
    context: Context,
    rawLabelOrZone: String,
    zoneToIanaMap: Map<String, String> = loadZoneToIanaMap(context)
): ZoneId? {
    val trimmed = rawLabelOrZone.trim()
    val candidateId = zoneToIanaMap[trimmed] ?: trimmed.replace(' ', '_')
    return runCatching { ZoneId.of(candidateId) }.getOrNull()
}
