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
        rvClassTeacher.layoutManager = LinearLayoutManager(this)
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
                        Toast.makeText(this@HomeTeacherActivity, "Gagal load kelas", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@HomeTeacherActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupShortcuts() {
        findViewById<LinearLayout>(R.id.btnBuatKelas).setOnClickListener {
            startActivity(Intent(this, CreateClassActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnTambahTugas).setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnUploadMateri).setOnClickListener {
            startActivity(Intent(this, UploadMaterialActivity::class.java))
        }
    }

    private fun setupNavbar() {
        // Set home active, others inactive
        setNavActive(R.id.btnHome)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            setNavActive(R.id.btnHome)
            // sudah di home
        }

        findViewById<ImageButton>(R.id.btnClass).setOnClickListener {
            setNavActive(R.id.btnClass)
            startActivity(Intent(this, TeacherClassActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnNotif).setOnClickListener {
            setNavActive(R.id.btnNotif)
            startActivity(Intent(this, TeacherReminderActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener {
            setNavActive(R.id.btnProfile)
            startActivity(Intent(this, ProfileTeacherActivity::class.java))
        }
    }

    private fun setNavActive(activeId: Int) {
        val buttons = listOf(R.id.btnHome, R.id.btnClass, R.id.btnNotif, R.id.btnProfile)
        buttons.forEach { id ->
            val btn = findViewById<ImageButton>(id)
            if (id == activeId) {
                btn.setBackgroundResource(R.drawable.bg_nav_active)
                btn.setColorFilter(resources.getColor(android.R.color.white, theme))
            } else {
                btn.setBackgroundResource(android.R.color.transparent)
                btn.setColorFilter(resources.getColor(android.R.color.white, theme))
                btn.alpha = 0.6f
            }
        }
        findViewById<ImageButton>(activeId).alpha = 1.0f
    }
}
