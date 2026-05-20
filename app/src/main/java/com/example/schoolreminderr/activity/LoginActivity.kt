package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.LoginRequest
import com.example.schoolreminderr.model.LoginResponse
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(
            email = email,
            password = password
        )

        RetrofitClient.getInstance(this).login(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        if (result.status) {
                            sessionManager.saveToken(result.token!!)
                            sessionManager.saveRole(result.user!!.role)

                            val intent = if (result.user.role == "guru") {
                                Intent(this@LoginActivity, HomeTeacherActivity::class.java)
                            } else {
                                Intent(this@LoginActivity, HomeStudentActivity::class.java)
                            }
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                result.message ?: "Login gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Response error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}