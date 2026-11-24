// MainActivity.kt
package com.example.zegarek

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logEvent("onCreate")
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ZoneSelectionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        logEvent("onStart")
    }

    override fun onResume() {
        super.onResume()
        logEvent("onResume")
    }

    override fun onPause() {
        super.onPause()
        logEvent("onPause")
    }

    override fun onStop() {
        super.onStop()
        logEvent("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        logEvent("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        logEvent("onDestroy")
    }

    private fun logEvent(methodName: String) {
        Log.d("ActivityLifecycle", "MainActivity -> $methodName")
    }
}

@Composable
fun ZoneSelectionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val availableZones: List<String> = stringArrayResource(R.array.available_zones).toList()
    val zoneToIanaMap = remember(context, availableZones) { loadZoneToIanaMap(context) }
    var selectedZones by rememberSaveable(availableZones) {
        mutableStateOf(availableZones.take(3))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.title_time_zones),
            style = MaterialTheme.typography.headlineSmall
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(availableZones, key = { it }) { zoneLabel ->
                val cityName = rememberCityName(zoneLabel)
                val ianaZone = zoneToIanaMap[zoneLabel.trim()] ?: zoneLabel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = zoneLabel in selectedZones,
                        onCheckedChange = {
                            selectedZones = toggleZoneSelection(selectedZones, zoneLabel)
                        }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cityName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = zoneLabel,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                val intent = Intent(context, TimeActivity::class.java).apply {
                    putExtra(EXTRA_SELECTED_ZONES, selectedZones.toTypedArray())
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.action_show_time))
        }
    }
}