package com.android.mistong

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn1)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn2)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn3)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn4)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn5)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn6)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn7)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn8)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn9)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn10)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }
        findViewById<Button>(R.id.btn11)
                .setOnClickListener {
                    startActivity(Intent(MainActivity@this, Sub1Activity::class.java))
                }


    }
}
