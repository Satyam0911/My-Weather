package com.example.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.content.Intent
import java.util.logging.Handler

class SplashView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_view)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}