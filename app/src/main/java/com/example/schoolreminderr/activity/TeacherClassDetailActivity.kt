package com.example.schoolreminderr.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.AssignmentData
import com.example.schoolreminderr.model.ClassMembersResponse
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.MaterialData
import com.example.schoolreminderr.model.User
import com.example.schoolreminderr.activity.SubmissionTeacherActivity
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherClassDetailActivity : AppCompatActivity() {

    private var classId: Int = 0
    private var className: String = ""
    private var classInfo: String = ""

    private lateinit var rvContent: RecyclerView
    private lateinit var tabTugas: TextView
    private lateinit var tabMateri: TextView
    private lateinit var tabSiswa: TextView

    private var assignments: List<AssignmentData> = emptyList()
    private var materials: List<MaterialData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class_task)

        classId   = intent.getIntExtra("CLASS_ID", 0)
        className = intent.getStringExtra("CLASS_NAME") ?: ""
        classInfo = intent.getStringExtra("CLASS_INFO") ?: ""

        rvContent = findViewById(R.id.rvTeacherTask)
        tabTugas  = findViewById(R.id.tabTugas)
        tabMateri = findViewById(R.id.tabMateri)
        tabSiswa  = findViewById(R.id.tabSiswa)

        rvContent.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnLink).setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("kode", classInfo))
            Toast.makeText(this, "Info kelas disalin", Toast.LENGTH_SHORT).show()
        }

        setupTabs()
        loadClassDetail()
    }

    private fun setupTabs() {
        selectTab(tabTugas)
        tabTugas.setOnClickListener { selectTab(tabTugas); showTugas() }
        tabMateri.setOnClickListener { selectTab(tabMateri); showMateri() }
        tabSiswa.setOnClickListener { selectTab(tabSiswa); loadMembers() }
    }

    private fun selectTab(selected: TextView) {
        listOf(tabTugas, tabMateri, tabSiswa).forEach { it.alpha = 0.45f }
        selected.alpha = 1.0f
    }

    private fun loadClassDetail() {
        RetrofitClient.getInstance(this).getClasses()
            .enqueue(object : Callback<ClassroomResponse> {
                override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val found = response.body()!!.data.find { it.id == classId }
                        assignments = found?.assignments ?: emptyList()
                        materials   = found?.materials   ?: emptyList()
                        showTugas()
                    } else {
                        Toast.makeText(this@TeacherClassDetailActivity, "Gagal memuat data kelas", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@TeacherClassDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadMembers() {
        RetrofitClient.getInstance(this).getClassMembers(classId)
            .enqueue(object : Callback<ClassMembersResponse> {
                override fun onResponse(call: Call<ClassMembersResponse>, response: Response<ClassMembersResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        showSiswa(response.body()!!.students ?: emptyList())
                    } else {
                        Toast.makeText(this@TeacherClassDetailActivity, "Gagal memuat siswa", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ClassMembersResponse>, t: Throwable) {
                    Toast.makeText(this@TeacherClassDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showTugas()  { rvContent.adapter = TugasAdapter(assignments) }
    private fun showMateri() { rvContent.adapter = MateriAdapter(materials) }
    private fun showSiswa(students: List<User>) { rvContent.adapter = SiswaAdapter(students) }

    inner class TugasAdapter(private val list: List<AssignmentData>) : RecyclerView.Adapter<TugasAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tvTitle: TextView = v.findViewById(R.id.tvTitle)
            val tvDeadline: TextView = v.findViewById(R.id.tvDeadline)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_teacher_task, parent, false))
        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.tvTitle.text    = list[position].title
            holder.tvDeadline.text = list[position].deadline ?: "-"

            // tambahkan ini
            holder.itemView.setOnClickListener {
                val intent = android.content.Intent(
                    holder.itemView.context,
                    SubmissionTeacherActivity::class.java
                )
                intent.putExtra("ASSIGNMENT_ID", list[position].id)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    inner class MateriAdapter(private val list: List<MaterialData>) : RecyclerView.Adapter<MateriAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tvTitle: TextView = v.findViewById(R.id.tvTitle)
            val btnOpen: Button   = v.findViewById(R.id.btnOpen)  // ganti dari tvDeadline
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_teacher_material, parent, false))  // ganti layout
        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.tvTitle.text = list[position].title
            holder.btnOpen.setOnClickListener {
                // buka file materi kalau ada
                val file = list[position].file
                if (file != null) {
                    val url = "http://192.168.100.6:8000/storage/$file"
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(url)
                    holder.itemView.context.startActivity(intent)
                } else {
                    android.widget.Toast.makeText(
                        holder.itemView.context,
                        "Tidak ada file lampiran",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    inner class SiswaAdapter(private val list: List<User>) : RecyclerView.Adapter<SiswaAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tvTitle: TextView = v.findViewById(R.id.tvTitle)
            val tvDeadline: TextView = v.findViewById(R.id.tvDeadline)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_teacher_task, parent, false))
        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.tvTitle.text    = list[position].name
            holder.tvDeadline.text = list[position].email ?: "-"
        }
    }
}