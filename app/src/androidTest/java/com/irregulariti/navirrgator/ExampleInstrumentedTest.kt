package com.irregulariti.navirrgator

import android.net.wifi.ScanResult
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.irregulariti.navirrgator", appContext.packageName)
    }

    @Test
    fun testStage(){
        assertEquals(4,returnStage("404"))
    }

    @Test
    fun testFindTheNearest(){
        assertEquals("вход", findTheNearest(listOf<ScanResult>()))
    }

    @Test
    fun testFindTheWay(){
        assertEquals(listOf("выход", "лифты 1 этаж", "1 и 2", "лифты 2 этаж", "2 и 3", "лифты 3", "3 и 4", "лифты 4 этаж", "404"), findTheWay("вход", "404"))
    }


}