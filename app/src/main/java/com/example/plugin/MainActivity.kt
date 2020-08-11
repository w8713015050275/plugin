package com.example.plugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zhangmen.libanotation.MyAnotation

@MyAnotation
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        testTraceClassVistor()
    }

    private fun testTraceClassVistor() {
        View(this)

    }
}
