package com.irregulariti.navirrgator

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.irregulariti.navirrgator.ui.theme.NavirrgatorTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            var ok = false
            var stage by remember { mutableStateOf(0) }
            var point by remember { mutableStateOf("") }
            var way by remember { mutableStateOf(mutableListOf<String>()) }

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
                stage = returnStage(findTheNearest(results)).also {
                    point = findTheNearest(results)
                } // current stage
            }

            fun scanFailure() { // permission menu
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

            if (stage == 1) {
                val firstColumn = mapOf(
                    "100а" to 1f,
                    "лестница1" to 1f,
                    "?1" to 1f,
                    "кафе лесное" to 2f,
                    "?2" to 1f,
                    "104" to 1f,
                    "выход" to 3f,
                    "106" to 1f,
                    "108" to 1f,
                    "?3" to 1f,
                    "110" to 1f,
                    "110а" to 1f,
                    "3" to 1f
                )
                val secondColumn = mapOf("" to 13f, "chill" to 1f)
                val thirdColumn = mapOf(
                    "100.1" to 1f,
                    "женский 1 этаж" to 1f,
                    "администрация" to 1f,
                    "?4" to 0.5f,
                    "101" to 1f,
                    "?5" to 0.5f,
                    "102" to 1f,
                    "103" to 1f,
                    "лифты 1 этаж" to 3f,
                    "105" to 1f,
                    "?6" to 1f,
                    "107" to 1f,
                    "111" to 1f,
                    "?7" to 1f,
                    "мужской 1 этаж" to 1f,
                    "109" to 1f
                )
                println(way)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 5.dp),
                        painter = painterResource(id = R.drawable.front_name),
                        contentDescription = null
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 100.dp),
                    Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        for (i in 0..12) {
                            val color = if (firstColumn.keys.toList()[i] !in way) {
                                ButtonDefaults.buttonColors(backgroundColor = Color(0xA9FDFCFD))
                            } else {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = Color(
                                        0xA9FC00FC
                                    )
                                )
                            }
                            Button(
                                modifier = Modifier.weight(firstColumn.values.toList()[i])
                                    .fillMaxSize(),
                                onClick = { way = findTheWay(point, firstColumn.keys.toList()[i]) },
                                colors = color,
                                border = BorderStroke(2.dp, Color(red = 0f, green = 0f, blue = 0f))
                            ) {
                                Text(firstColumn.keys.toList()[i])
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        for (i in 0..1) {
                            Box(
                                modifier = Modifier.weight(secondColumn.values.toList()[i])
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(secondColumn.keys.toList()[i])
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize().weight(1f)
                    ) {
                        for (i in 0..15) {
                            val color = if (thirdColumn.keys.toList()[i] !in way) {
                                ButtonDefaults.buttonColors(backgroundColor = Color(0xA9FDFCFD))
                            } else {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = Color(
                                        0xA9FC00FC
                                    )
                                )
                            }
                            Button(
                                modifier = Modifier.weight(thirdColumn.values.toList()[i])
                                    .fillMaxSize(),
                                onClick = { way = findTheWay(point, thirdColumn.keys.toList()[i]) },
                                colors = color,
                                border = BorderStroke(
                                    2.7.dp,
                                    Color(red = 0f, green = 0f, blue = 0f)
                                )
                            ) {
                                Text(
                                    thirdColumn.keys.toList()[i],
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
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
fun findTheNearest(results: List<ScanResult>): String {
    var currentPoint = ""
    val currentMap = mutableMapOf<String, Int>()
    for (i in results) {
        currentMap[i.SSID] = i.level * (-1)
    }
    val levels = Valuable().levels
    val names = Valuable().names
    val searching = mutableMapOf<Int, Int>()
    for (i in levels.indices) {
        searching[i] = levels[i].keys.toSet().intersect(currentMap.keys.toSet()).size
    }
    val assertion = searching.keys.sortedBy { searching[it] }.reversed()
    val curLevels = mutableListOf<Map<String, Int>>()
    val curNames = mutableListOf<String>()
    for (i in assertion.subList(0, minOf(assertion.size, 10))) {
        curLevels.add(levels[i])
        curNames.add(names[i])
    }
    val near = mutableListOf<Int>()
    for (ind in curLevels.indices) {
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
    return currentPoint
}

fun findTheWay(from: String, to: String): MutableList<String> {
    var diff = diffStages(from, to)
    var temp = mutableListOf<MutableList<String>>()
    for (i in Valuable().graph[from]!!) {
        temp.add(mutableListOf(i))
    }
    while (true) {
        var list = mutableListOf<MutableList<String>>()
        for (i in temp.indices) {
            var ok = false
            for (j in Valuable().graph[temp[i].last()]!!) {
                val inside = temp[i].toMutableList()
                if (j != temp[i].last() && j !in inside) {
                    if ("1 и 2" in j || "2 и 3" in j || "3 и 4" in j || "4 и 5" in j) {
                        if (diff != 0) {
                            diff--
                            inside.add(j)
                            list = mutableListOf(inside)
                            ok = true
                            break
                        }
                    } else {
                        inside.add(j)
                        list.add(inside)
                        if (to in inside) {
                            return inside
                        }
                    }
                }
            }
            if (ok) break
        }
        temp = list.toMutableList()
        println(temp)
    }
}

fun diffStages(from: String, to: String): Int {
    return kotlin.math.abs(returnStage(from) - returnStage(to))
}

fun returnStage(point: String): Int{
    var stage = 0
    val ind = Valuable().graph.keys.indexOf(point)
    if (ind <= 29) {
        stage = 1
    } else if (ind in 31..43) {
        stage = 2
    } else if (ind in 45..60) {
        stage = 3
    } else if (ind in 62..75) {
        stage = 4
    } else if (ind in 77..90) {
        stage = 5
    }
    return stage
}