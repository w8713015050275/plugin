package com.example.plugin

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.runners.JUnit4

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleInstrumentedTest {

//        internal var calculater = Calculater()

        @org.junit.Test
        fun testAdd() {
            val a = 1
            val b = 2

            val result = 3

            Assert.assertEquals(result, 3) // 验证result==3，如果不正确，测试不通过
        }
}
