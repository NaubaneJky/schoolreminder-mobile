package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        val token   = session.getToken()
        val role    = session.getRole()

        val intent = when {
            token == null -> Intent(this, LoginActivity::class.java)
            role == "guru" -> Intent(this, HomeTeacherActivity::class.java)
            else -> Intent(this, HomeStudentActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}