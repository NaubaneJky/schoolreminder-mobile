package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.RegisterRequest
import com.example.schoolreminderr.model.RegisterResponse
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etSchool: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnGuru: LinearLayout
    private lateinit var btnSiswa: LinearLayout
    private lateinit var sessionManager: SessionManager

    private var selectedRole: String = "siswa" // default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sessionManager = SessionManager(this)

        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etSchool = findViewById(R.id.etSchool)
        btnRegister = findViewById(R.id.btnRegister)
        btnGuru = findViewById(R.id.btnGuru)
        btnSiswa = findViewById(R.id.btnSiswa)

        // Role selection
        btnGuru.setOnClickListener {
            selectedRole = "guru"
            btnGuru.setBackgroundResource(R.drawable.bg_role_selected)
            btnSiswa.setBackgroundResource(R.drawable.bg_role_button)
        }

        btnSiswa.setOnClickListener {
            selectedRole = "siswa"
            btnSiswa.setBackgroundResource(R.drawable.bg_role_selected)
            btnGuru.setBackgroundResource(R.drawable.bg_role_button)
        }

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val school = etSchool.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || school.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(
            name = name,
            email = email,
            password = password,
            passwordConfirmation = password,  // sama dengan password
            role = selectedRole,
            school = school
        )

        RetrofitClient.getInstance(this).register(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        if (result.status) {
                            sessionManager.saveToken(result.token!!)
                            sessionManager.saveRole(result.user!!.role)

                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi berhasil!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = if (result.user.role == "guru") {
                                Intent(this@RegisterActivity, HomeTeacherActivity::class.java)
                            } else {
                                Intent(this@RegisterActivity, HomeStudentActivity::class.java)
                            }
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                result.message ?: "Registrasi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}