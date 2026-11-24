// CityNameResolver.kt
package com.example.zegarek

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

fun resolveCityNameFromResources(
    context: Context,
    rawLabelOrZone: String,
    zoneToIanaMap: Map<String, String> = loadZoneToIanaMap(context)
): String {
    val trimmed = rawLabelOrZone.trim()
    val ianaId = zoneToIanaMap[trimmed] ?: trimmed.replace(' ', '_')
    val keySource = if (trimmed in zoneToIanaMap) ianaId else trimmed

    val normalizedKey = "city_" + keySource
        .lowercase(Locale.ROOT)
        .replace(':', '_')
        .replace('/', '_')
        .replace(' ', '_')
        .replace('-', '_')
        .replace(Regex("_+"), "_")
        .trim('_')

    val resId = context.resources.getIdentifier(
        normalizedKey,
        "string",
        context.packageName
    )

    return if (resId != 0) {
        context.getString(resId)
    } else {
        ianaId.substringAfterLast('/').replace('_', ' ')
    }
}

@Composable
fun rememberCityName(rawLabelOrZone: String): String {
    val context = LocalContext.current
    val zoneMap = remember(context) { loadZoneToIanaMap(context) }
    return remember(context, rawLabelOrZone, zoneMap) {
        resolveCityNameFromResources(context, rawLabelOrZone, zoneMap)
    }
}
