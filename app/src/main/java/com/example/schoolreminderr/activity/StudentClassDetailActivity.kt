package com.example.schoolreminderr.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.AssignmentData
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.MaterialData
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentClassDetailActivity : AppCompatActivity() {

    private var classId: Int = 0
    private var className: String = ""
    private var subject: String = ""
    private var teacherName: String = ""

    private lateinit var rvContent: RecyclerView
    private lateinit var tabTugas: TextView
    private lateinit var tabMateri: TextView
    private lateinit var tvClassName: TextView
    private lateinit var tvSubject: TextView
    private lateinit var tvTeacher: TextView

    private var assignments: List<AssignmentData> = emptyList()
    private var materials: List<MaterialData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_class_task)

        // 1. Ambil data dari Intent (dikirim dari StudentClassAdapter)
        classId = intent.getIntExtra("CLASS_ID", 0)
        className = intent.getStringExtra("CLASS_NAME") ?: "Detail Kelas"
        subject = intent.getStringExtra("SUBJECT") ?: ""
        teacherName = intent.getStringExtra("TEACHER_NAME") ?: ""

        // 2. Inisialisasi View
        rvContent = findViewById(R.id.rvStudentTask)
        tabTugas = findViewById(R.id.tabTugas)
        tabMateri = findViewById(R.id.tabMateri)
        tvClassName = findViewById(R.id.tvClassName)
        tvSubject = findViewById(R.id.tvSubject)
        tvTeacher = findViewById(R.id.tvTeacher)

        // 3. Set Data Header
        tvClassName.text = className
        tvSubject.text = subject
        tvTeacher.text = "Guru: $teacherName"

        rvContent.layoutManager = LinearLayoutManager(this)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        setupTabs()
        loadClassDetail()
    }

    private fun setupTabs() {
        // Default Tab Tugas aktif
        selectTab(tabTugas)

        tabTugas.setOnClickListener {
            selectTab(tabTugas)
            showTugas()
        }

    }

    private fun selectTab(selected: TextView) {
        // Reset semua tab ke transparan (tidak aktif)
        tabTugas.alpha = 0.5f
        tabMateri.alpha = 0.5f

        // Set yang dipilih jadi terang
        selected.alpha = 1.0f
    }

    private fun loadClassDetail() {
        RetrofitClient.getInstance(this).getClasses()
            .enqueue(object : Callback<ClassroomResponse> {
                override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val found = response.body()!!.data.find { it.id == classId }
                        assignments = found?.assignments ?: emptyList()
                        materials = found?.materials ?: emptyList()

                        // Set data tambahan jika belum ada dari intent
                        if (subject.isEmpty()) tvSubject.text = found?.subject
                        if (teacherName.isEmpty()) tvTeacher.text = "Guru: ${found?.teacherName}"

                        showTugas() // Default tampilkan tugas
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@StudentClassDetailActivity, "Gagal memuat data: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showTugas() {
        rvContent.adapter = TugasAdapter(assignments)
    }



    // --- ADAPTER INTERNAL ---

    // GANTI TugasAdapter lama dengan ini
    inner class TugasAdapter(private val list: List<AssignmentData>) : RecyclerView.Adapter<TugasAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tvTitle: TextView = v.findViewById(R.id.tvTaskTitle)
            val tvDeadline: TextView = v.findViewById(R.id.tvDeadlineTag)
            val tvDescription: TextView = v.findViewById(R.id.tvTaskDescription)
            val btnAction: Button = v.findViewById(R.id.btnTaskAction)
            val layoutAttachment: View = v.findViewById(R.id.layoutAttachment)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_student_task, parent, false))

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = list[position]
            holder.tvTitle.text = item.title
            holder.tvDescription.text = item.description ?: "Tidak ada instruksi tambahan."

            // is_submitted didapat dari model data
            val isSubmitted = item.is_submitted ?: false
            val (tagText, colorHex, isCompleted) = getDeadlineLogic(item.deadline, isSubmitted)

            // 1. Atur Tag Deadline (Lingkaran kecil)
            holder.tvDeadline.text = tagText
            holder.tvDeadline.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor(colorHex)
            )

            // 2. Atur Tombol Aksi
            val colorInt = android.graphics.Color.parseColor(colorHex)
            val isExpired = tagText == "✕"

            when {
                isCompleted -> {
                    holder.btnAction.text = "TUGAS SELESAI"
                    holder.btnAction.backgroundTintList = android.content.res.ColorStateList.valueOf(colorInt)
                    holder.btnAction.isEnabled = false
                }
                isExpired -> {
                    holder.btnAction.text = "TIDAK MENGERJAKAN"
                    holder.btnAction.backgroundTintList = android.content.res.ColorStateList.valueOf(colorInt)
                    holder.btnAction.isEnabled = false
                }
                else -> {
                    holder.btnAction.text = "BUKA TUGAS"
                    holder.btnAction.backgroundTintList = android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor("#4A90E2")
                    )
                    holder.btnAction.isEnabled = true
                    holder.btnAction.setOnClickListener {
                        // Di sini Anda bisa mengarahkan ke StudentSubmissionActivity
                        Toast.makeText(this@StudentClassDetailActivity, "Membuka: ${item.title}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // --- LOGIKA LAMPIRAN FILE GURU ---
            val fileNameFromDb = item.file // Contoh: "assignments/xyz.jpg"
            if (!fileNameFromDb.isNullOrEmpty()) {
                holder.layoutAttachment.visibility = View.VISIBLE

                val tvFileName: TextView = holder.itemView.findViewById(R.id.tvFileName)

                // Hanya ambil nama filenya saja untuk ditampilkan (xyz.jpg)
                val displayFileName = fileNameFromDb.substringAfterLast("/")
                tvFileName.text = displayFileName

                // KETIKA DIKLIK -> DOWNLOAD FILE
                holder.layoutAttachment.setOnClickListener {
                    // URL harus menggunakan path lengkap dari DB
                    val url = "http://192.168.100.6:8000/storage/$fileNameFromDb"
                    downloadFile(fileNameFromDb, url)
                }
            } else {
                holder.layoutAttachment.visibility = View.GONE
            }
        }
    }

    private fun getDeadlineInfo(deadlineStr: String?): Pair<String, Int> {
        if (deadlineStr.isNullOrEmpty()) return Pair("-", -1)

        return try {
            // Asumsi format deadline dari API: "yyyy-MM-dd" atau "yyyy-MM-dd HH:mm:ss"
            val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val deadlineDate = format.parse(deadlineStr)
            val today = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }.time

            val diff = deadlineDate.time - today.time
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()

            when {
                days < 0 -> Pair("Selesai", -1)
                days == 0 -> Pair("Hari Ini", 0)
                else -> Pair("H-$days", days)
            }
        } catch (e: Exception) {
            Pair("-", -1)
        }
    }

    private fun getDeadlineLogic(deadlineStr: String?, isSubmitted: Boolean): Triple<String, String, Boolean> {
        val gray = "#757575"
        val red = "#FF5D5D"
        val green = "#4CAF50"
        val orange = "#FF6F00"
        val blue = "#59A9DC"
        val pink = "#E85F98"

        if (isSubmitted) return Triple("✓", green, true)
        if (deadlineStr.isNullOrEmpty()) return Triple("-", gray, false)

        return try {
            // 1. Coba deteksi apakah API kirim Jam (yyyy-MM-dd HH:mm:ss) atau hanya Tanggal (yyyy-MM-dd)
            val formatPattern = if (deadlineStr.contains(":")) "yyyy-MM-dd HH:mm:ss" else "yyyy-MM-dd"
            val sdf = java.text.SimpleDateFormat(formatPattern, java.util.Locale.getDefault())

            val deadlineDate = sdf.parse(deadlineStr) ?: return Triple("-", gray, false)
            val now = java.util.Date() // Waktu sekarang (termasuk jam & menit)

            // 2. CEK APAKAH SUDAH LEWAT (DIBANDINGKAN DETIK INI)
            if (deadlineDate.before(now)) {
                return Triple("✕", red, false) // Langsung silang jika sudah lewat satu detik pun
            }

            // 3. Hitung selisih hari untuk pewarnaan Tag
            val diffInMillies = deadlineDate.time - now.time
            val days = (diffInMillies / (1000 * 60 * 60 * 24)).toInt()

            when {
                // H-0 sampai H-6 (Mendekati deadline)
                days <= 6 -> {
                    val teks = if (days == 0) "Hari Ini" else "H-$days"
                    Triple(teks, red, false)
                }
                // H-7 sampai H-9
                days <= 9 -> Triple("H-$days", blue, false)
                // H-10 ke atas
                else -> Triple("H-$days", pink, false)
            }
        } catch (e: Exception) {
            Triple("-", gray, false)
        }
    }

    private fun downloadFile(fileNameInDb: String, url: String) {
        android.util.Log.d("CEK_NAMA_FILE", "Nama di DB: $fileNameInDb")
        android.util.Log.d("CEK_NAMA_FILE", "Full URL: $url")

        try {
            // Jika fileNameInDb adalah "assignments/tugas.jpg",
            // kita ambil bagian belakangnya saja ("tugas.jpg") untuk nama file di HP
            val cleanFileName = if (fileNameInDb.contains("/")) {
                fileNameInDb.substringAfterLast("/")
            } else {
                fileNameInDb
            }

            val uri = Uri.parse(url)
            val request = android.app.DownloadManager.Request(uri)
                .setTitle("Mendownload $cleanFileName")
                .setDescription("File Tugas")
                .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                // Simpan dengan nama yang bersih (tanpa folder assignments/)
                .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, cleanFileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .addRequestHeader("User-Agent", "Mozilla/5.0")

            request.allowScanningByMediaScanner()

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as android.app.DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(this, "Download dimulai...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            android.util.Log.e("DOWNLOAD_ERROR", e.message ?: "Error")
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}