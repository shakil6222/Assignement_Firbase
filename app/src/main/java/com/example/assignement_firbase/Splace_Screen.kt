package com.example.assignement_firbase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.assignement_firbase.LoginPage.Login_Page

class Splace_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splace_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,Login_Page::class.java))
            finish()
        }, 2000)

    }
}