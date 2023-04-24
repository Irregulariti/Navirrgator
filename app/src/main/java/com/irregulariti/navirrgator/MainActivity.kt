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
            findTheWay(findTheNearest(listOf()), "женский 1 этаж")
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
fun findTheNearest(results: List<ScanResult>): String{
    var currentPoint = ""
    val currentMap = mutableMapOf<String, Int>()
    for (i in results) {
        currentMap[i.SSID] = i.level * (-1)
    }
    val levels = Valuable().levels
    val names = Valuable().names
    val searching = mutableMapOf<Int,Int>()
    for (i in levels.indices) {
        searching[i] = levels[i].keys.toSet().intersect(currentMap.keys.toSet()).size
    }
    val assertion = searching.keys.sortedBy { searching[it] }.reversed()
    val curLevels = mutableListOf<Map<String,Int>>()
    val curNames = mutableListOf<String>()
    for (i in assertion.subList(0, minOf(assertion.size, 10))){
        curLevels.add(levels[i])
        curNames.add(names[i])
    }
    val near = mutableListOf<Int>()
    for (ind in curLevels.indices){
        var ans = 0
        for (i in curLevels[ind].keys) {
            if (i in currentMap.keys) {
                ans += ((curLevels[ind][i]!! - currentMap[i]!!) * (curLevels[ind][i]!! - currentMap[i]!!))
            }
        }
        near.add(ans)
    }
    //println(near)
    currentPoint = curNames[near.indexOf(near.min())]
    println(currentPoint)
    return currentPoint
}

fun findTheWay(from: String, to: String): MutableList<String> {
    var temp = mutableListOf<MutableList<String>>()
    for (i in Valuable().graph[from]!!){
        temp.add(mutableListOf(i))
    }
    while (true){
        val list = mutableListOf<MutableList<String>>()
        for (i in temp.indices){
            println(temp[i])
            for (j in Valuable().graph[temp[i].last()]!!){
                val inside = temp[i].toMutableList()
                if (j != temp[i].last() && j !in inside) {
                    inside.add(j)
                    list.add(inside)
                    if (to in inside){
                        println(inside)
                        return inside
                    }
                }
            }
        }
        temp = list.toMutableList()
    }
}

fun diffStages(from: String, to: String){
    return
}


