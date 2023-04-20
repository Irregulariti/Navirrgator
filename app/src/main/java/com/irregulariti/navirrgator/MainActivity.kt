package com.irregulariti.navirrgator

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.irregulariti.navirrgator.ui.theme.NavirrgatorTheme

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val wifiManager: WifiManager = getSystemService(WIFI_SERVICE) as WifiManager
                fun scanSuccess() {
                    val results = if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        wifiManager.scanResults
                    } else {
                        listOf()
                    }
                    findTheNearest(results)
                }

                fun scanFailure() {
                    val results = wifiManager.scanResults
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                    scanSuccess()
                }

                val success = wifiManager.startScan()
//                println(success)
                if (!success) {
                    scanFailure()
                }

                val wifiScanReceiver = object : BroadcastReceiver() {

                    override fun onReceive(context: Context, intent: Intent) {
                        val success =
                            intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                        if (success) {
                            scanSuccess()
                        } else {
                            scanFailure()
                        }
                    }
                }

                val intentFilter = IntentFilter()
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                registerReceiver(wifiScanReceiver, intentFilter)
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavirrgatorTheme {
        Greeting("Android")
    }
}

@Suppress("DEPRECATION")
fun findTheNearest(results: List<ScanResult>){
    val currentPoint = ""
    val currentMap = mutableMapOf<String, Int>()
    for (i in results) {
        currentMap[i.SSID] = i.level * (-1)
    }
    println(currentMap)
}