package com.example.schoolreminderr.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button // Pastikan import Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.StudentClassAdapter
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.TeacherClass
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentClassActivity : AppCompatActivity() {

    private lateinit var rvClass: RecyclerView
    private var classList = ArrayList<TeacherClass>()
    private lateinit var adapter: StudentClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_student)

        // Inisialisasi RecyclerView
        rvClass = findViewById(R.id.rvClass)
        adapter = StudentClassAdapter(classList)
        rvClass.layoutManager = LinearLayoutManager(this)
        rvClass.adapter = adapter

        // --- PERBAIKAN DI SINI ---
        // Gunakan ID btnJoin sesuai dengan XML terbaru Anda
        val btnJoin = findViewById<Button>(R.id.btnGabung)
        btnJoin.setOnClickListener {
            // Arahkan ke JoinClassActivity
            val intent = Intent(this, JoinClassActivity::class.java)
            startActivity(intent)
        }
        // -------------------------

        setupNavbar()
        loadClasses()
    }

    override fun onResume() {
        super.onResume()
        loadClasses() // Refresh otomatis setiap kali masuk ke halaman ini
    }

    private fun loadClasses() {
        RetrofitClient.getInstance(this).getClasses().enqueue(object : Callback<ClassroomResponse> {
            override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                android.util.Log.d("API_CHECK", "Response Code: ${response.code()}")
                android.util.Log.d("API_CHECK", "JSON Body: ${response.body()?.toString()}")
                if (response.isSuccessful && response.body() != null) {
                    classList.clear()
                    val data = response.body()!!.data

                    if (data.isEmpty()) {
                        Toast.makeText(this@StudentClassActivity, "Kamu belum bergabung di kelas manapun", Toast.LENGTH_LONG).show()
                    } else {
                        data.forEach {
                            classList.add(TeacherClass(
                                id = it.id,
                                name = it.name,
                                info = "${it.subject} • ${it.class_code}"
                            ))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                Toast.makeText(this@StudentClassActivity, "Gagal memuat: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupNavbar() {
        setNavActive(R.id.btnClass)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, HomeStudentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnClass).setOnClickListener { /* Tetap Kosong */ }

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
        val navIds = listOf(R.id.btnHome, R.id.btnClass, R.id.btnNotif, R.id.btnProfile)
        navIds.forEach { id ->
            findViewById<ImageButton>(id)?.apply {
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