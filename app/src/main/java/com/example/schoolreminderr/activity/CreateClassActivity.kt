package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.CreateClassRequest
import com.example.schoolreminderr.model.CreateClassResponse
import com.example.schoolreminderr.network.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateClassActivity : AppCompatActivity() {

    private lateinit var etClassName: EditText
    private lateinit var etLevel: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_class)

        etClassName = findViewById(R.id.etClassName)
        etLevel = findViewById(R.id.etLevel)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            createClass()
        }
    }

    private fun createClass() {

        val name = etClassName.text.toString().trim()
        val subject = etLevel.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (
            name.isEmpty() ||
            subject.isEmpty() ||
            description.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Semua field wajib diisi",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val request = CreateClassRequest(
            name = name,
            subject = subject,
            description = description
        )

        RetrofitClient.getInstance(this)
            .createClass(request)
            .enqueue(object : Callback<CreateClassResponse> {

                override fun onResponse(
                    call: Call<CreateClassResponse>,
                    response: Response<CreateClassResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body() != null
                    ) {

                        val result = response.body()!!

                        if (result.status) {

                            Toast.makeText(
                                this@CreateClassActivity,
                                result.message,
                                Toast.LENGTH_LONG
                            ).show()

                            finish()

                        } else {

                            Toast.makeText(
                                this@CreateClassActivity,
                                response.errorBody()?.string(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        Toast.makeText(
                            this@CreateClassActivity,
                            "Error ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<CreateClassResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@CreateClassActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}