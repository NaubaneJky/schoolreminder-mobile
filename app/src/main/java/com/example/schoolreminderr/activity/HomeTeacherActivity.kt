package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.TeacherClassAdapter
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.TeacherClass
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.utils.SessionManager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeTeacherActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var rvClassTeacher: RecyclerView

    private var classList = ArrayList<TeacherClass>()
    private lateinit var adapter: TeacherClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_teacher)

        sessionManager = SessionManager(this)

        rvClassTeacher = findViewById(R.id.rvClassTeacher)

        adapter = TeacherClassAdapter(classList)

        rvClassTeacher.layoutManager =
            LinearLayoutManager(this)

        rvClassTeacher.adapter = adapter

        setupShortcuts()
        setupNavbar()

        loadClasses()
    }

    override fun onResume() {
        super.onResume()

        loadClasses()
    }

    private fun loadClasses() {

        RetrofitClient.getInstance(this)
            .getClasses()
            .enqueue(object : Callback<ClassroomResponse> {

                override fun onResponse(
                    call: Call<ClassroomResponse>,
                    response: Response<ClassroomResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body() != null
                    ) {

                        classList.clear()

                        response.body()!!.data.forEach {

                            classList.add(
                                TeacherClass(
                                    id = it.id,
                                    name = it.name,
                                    info = "${it.subject} • ${it.class_code}"
                                )
                            )
                        }

                        adapter.notifyDataSetChanged()

                    } else {

                        Toast.makeText(
                            this@HomeTeacherActivity,
                            "Gagal load kelas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ClassroomResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@HomeTeacherActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun setupShortcuts() {

        val btnBuatKelas =
            findViewById<LinearLayout>(R.id.btnBuatKelas)

        btnBuatKelas.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CreateClassActivity::class.java
                )
            )
        }

        val btnTambahTugas =
            findViewById<LinearLayout>(R.id.btnTambahTugas)

        btnTambahTugas.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CreateTaskActivity::class.java
                )
            )
        }

        val btnUploadMateri =
            findViewById<LinearLayout>(R.id.btnUploadMateri)

        btnUploadMateri.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UploadMaterialActivity::class.java
                )
            )
        }
    }

    private fun setupNavbar() {

        val navHome =
            findViewById<LinearLayout>(R.id.navHome)

        val navTask =
            findViewById<LinearLayout>(R.id.navTask)

        val navMaterial =
            findViewById<LinearLayout>(R.id.navMaterial)

        val navProfile =
            findViewById<LinearLayout>(R.id.navProfile)

        navHome.setOnClickListener {
            // stay here
        }

        navTask.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TeacherReminderActivity::class.java
                )
            )
        }

        navMaterial.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UploadMaterialActivity::class.java
                )
            )
        }

        navProfile.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileTeacherActivity::class.java
                )
            )
        }
    }
}