// TimeActivity.kt
package com.example.zegarek

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class TimeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logEvent("onCreate")

        val receivedZones: List<String> = intent
            ?.getStringArrayExtra(EXTRA_SELECTED_ZONES)
            ?.toList()
            ?: emptyList()

        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReceivedZonesPreviewScreen(
                        zones = receivedZones,
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
        Log.d("ActivityLifecycle", "TimeActivity -> $methodName")
    }
}

@Composable
fun ReceivedZonesPreviewScreen(
    zones: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Wybrane strefy:",
            style = MaterialTheme.typography.headlineSmall
        )

        if (zones.isEmpty()) {
            Text(text = "Brak przekazanych stref.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(zones, key = { it }) { zoneId ->
                    Text(text = zoneId, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}