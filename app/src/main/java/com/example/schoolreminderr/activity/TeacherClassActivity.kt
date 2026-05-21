package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherClassActivity : AppCompatActivity() {

    private lateinit var rvClass: RecyclerView
    private var classList = ArrayList<TeacherClass>()
    private lateinit var adapter: TeacherClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class)

        rvClass = findViewById(R.id.rvTeacherClass)
        adapter = TeacherClassAdapter(classList)
        rvClass.layoutManager = LinearLayoutManager(this)
        rvClass.adapter = adapter

        findViewById<LinearLayout>(R.id.btnCreateClass).setOnClickListener {
            startActivity(Intent(this, CreateClassActivity::class.java))
        }

        setupNavbar()
        loadClasses()
    }

    override fun onResume() {
        super.onResume()
        loadClasses()
    }

    private fun loadClasses() {
        RetrofitClient.getInstance(this).getClasses()
            .enqueue(object : Callback<ClassroomResponse> {
                override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        classList.clear()
                        response.body()!!.data.forEach {
                            classList.add(TeacherClass(id = it.id, name = it.name, info = "${it.subject} • ${it.class_code}"))
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@TeacherClassActivity, "Gagal load kelas", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@TeacherClassActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupNavbar() {
        setNavActive(R.id.btnClass)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            startActivity(Intent(this, HomeTeacherActivity::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.btnClass).setOnClickListener {
            // sudah di sini
        }

        findViewById<ImageButton>(R.id.btnNotif).setOnClickListener {
            startActivity(Intent(this, TeacherReminderActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileTeacherActivity::class.java))
        }
    }

    private fun setNavActive(activeId: Int) {
        listOf(R.id.btnHome, R.id.btnClass, R.id.btnNotif, R.id.btnProfile).forEach { id ->
            val btn = findViewById<ImageButton>(id)
            btn.setBackgroundResource(android.R.color.transparent)
            btn.alpha = 0.6f
        }
        findViewById<ImageButton>(activeId).apply {
            setBackgroundResource(R.drawable.bg_nav_active)
            alpha = 1.0f
        }
    }
}