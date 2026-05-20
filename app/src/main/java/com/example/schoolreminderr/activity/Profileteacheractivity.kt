package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.utils.SessionManager

class ProfileTeacherActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_teacher)

        sessionManager = SessionManager(this)

        setupNavbar()
    }

    private fun setupNavbar() {
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navTask = findViewById<LinearLayout>(R.id.navTask)
        val navMaterial = findViewById<LinearLayout>(R.id.navMaterial)
        val navProfile = findViewById<LinearLayout>(R.id.navProfile)

        navHome?.setOnClickListener {
            startActivity(android.content.Intent(this, HomeTeacherActivity::class.java))
            finish()
        }

        navTask?.setOnClickListener {
            startActivity(android.content.Intent(this, TeacherReminderActivity::class.java))
        }

        navMaterial?.setOnClickListener {
            startActivity(android.content.Intent(this, UploadMaterialActivity::class.java))
        }

        navProfile?.setOnClickListener {
            // sudah di profil
        }
    }
}