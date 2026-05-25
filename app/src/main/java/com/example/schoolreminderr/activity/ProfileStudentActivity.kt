package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ProfileResponse
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStudentActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var tvName: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvSchool: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_student)

        sessionManager = SessionManager(this)

        tvName = findViewById(R.id.tvName)
        tvFullName = findViewById(R.id.tvFullName)
        tvGender = findViewById(R.id.tvGender)
        tvEmail = findViewById(R.id.tvEmail)
        tvSchool = findViewById(R.id.tvSchool)


        setupNavbar()
        loadProfile()
    }

    private fun loadProfile() {
        RetrofitClient.getInstance(this)
            .getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!.user
                        tvName.text = user.name
                        tvFullName.text = user.name
                        tvGender.text = user.gender ?: "-"
                        tvEmail.text = user.email
                        tvSchool.text = user.school ?: "-"
                    } else {
                        Toast.makeText(this@ProfileStudentActivity, "Gagal load profile", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileStudentActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupNavbar() {
        setNavActive(R.id.btnProfile)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, HomeStudentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

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

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener { /* Tetap Kosong */ }
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