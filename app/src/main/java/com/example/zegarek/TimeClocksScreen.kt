// TimeClocksScreen.kt
package com.example.zegarek

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

private fun millisUntilNextFullSecond(nowMillis: Long = System.currentTimeMillis()): Long {
    val remainder = nowMillis % 1000L
    return if (remainder == 0L) 1000L else 1000L - remainder
}

@Composable
fun TimeClocksScreen(
    selectedZoneIds: List<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val zoneToIanaMap: Map<String, String> = remember(context as Any) {
        loadZoneToIanaMap(context)
    }
    var now: Instant by remember {
        mutableStateOf(Instant.ofEpochMilli(System.currentTimeMillis()))
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                val currentMillis = System.currentTimeMillis()
                now = Instant.ofEpochMilli(currentMillis)
                Log.d("ClockTick", "Tick at $currentMillis")
                delay(millisUntilNextFullSecond(currentMillis).milliseconds)
            }
        }
    }

    val state: WorldClockUiState = buildWorldClockUiState(
        now = now,
        selectedZoneIds = selectedZoneIds,
        zoneToIanaMap = zoneToIanaMap
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = state.localTime,
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = state.localDate,
            style = MaterialTheme.typography.bodyLarge
        )

        if (state.zones.isEmpty()) {
            Text(text = stringResource(R.string.no_zones_selected))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.zones,
                    key = { item: ZoneClockItem -> item.zoneId }
                ) { zoneItem: ZoneClockItem ->
                    val cityName: String = rememberCityName(zoneItem.zoneId)
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = cityName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = zoneItem.zoneId,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = zoneItem.currentTime,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = zoneItem.diffFromLocal,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}