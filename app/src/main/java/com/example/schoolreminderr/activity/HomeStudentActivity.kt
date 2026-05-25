package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.utils.SessionManager

class HomeStudentActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_student)

        sessionManager = SessionManager(this)

        setupNavbar()
    }

    private fun setupNavbar() {
        setNavActive(R.id.btnHome)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener { /* Tetap Kosong */ }

        findViewById<ImageButton>(R.id.btnClass).setOnClickListener {
            val intent = Intent(this, StudentClassActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnNotif).setOnClickListener {
            val intent = Intent(this, StudentReminderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            val intent = Intent(this, ProfileStudentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }

    private fun setNavActive(activeId: Int) {
        val buttons = listOf(R.id.btnHome, R.id.btnClass, R.id.btnNotif, R.id.btnProfile)
        buttons.forEach { id ->
            val btn = findViewById<ImageButton>(id)
            btn?.apply {
                setBackgroundResource(android.R.color.transparent)
                alpha = 0.6f
            }
        }
        findViewById<ImageButton>(activeId)?.apply {
            setBackgroundResource(R.drawable.bg_nav_active)
            alpha = 1.0f
        }
    }
}