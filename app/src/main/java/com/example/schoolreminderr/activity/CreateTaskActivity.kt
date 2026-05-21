package com.example.schoolreminderr.activity

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.CreateAssignmentResponse
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.receiver.DeadlineReceiver
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var tvClass: TextView
    private lateinit var tvDeadline: TextView
    private lateinit var tvFileName: TextView
    private lateinit var btnUpload: LinearLayout
    private lateinit var btnSaveTask: Button
    private lateinit var btnBack: ImageView

    private var selectedClassId: Int = -1
    private var selectedDeadline: String = ""
    private var selectedDeadlineMillis: Long = 0L
    private var selectedFileUri: Uri? = null

    private val classIds = ArrayList<Int>()
    private val classNames = ArrayList<String>()

    // File picker
    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedFileUri = it
            val name = getFileName(it)
            tvFileName.text = name
            tvFileName.visibility = TextView.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        etTitle       = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        tvClass       = findViewById(R.id.tvClass)
        tvDeadline    = findViewById(R.id.tvDeadline)
        btnSaveTask   = findViewById(R.id.btnSaveTask)
        btnBack       = findViewById(R.id.btnBack)
        btnUpload     = findViewById(R.id.btnUpload)

        // TextView untuk nama file — tambahkan di layout kalau belum ada
        tvFileName = TextView(this).apply {
            visibility = TextView.GONE
            setTextColor(resources.getColor(android.R.color.holo_blue_dark, theme))
        }
        btnUpload.addView(tvFileName)

        btnBack.setOnClickListener { finish() }
        tvClass.setOnClickListener { showClassPicker() }
        tvDeadline.setOnClickListener { showDatePicker() }
        btnUpload.setOnClickListener { filePicker.launch("*/*") }
        btnSaveTask.setOnClickListener { saveTask() }

        loadClasses()
    }

    private fun loadClasses() {
        RetrofitClient.getInstance(this).getClasses()
            .enqueue(object : Callback<ClassroomResponse> {
                override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        classIds.clear()
                        classNames.clear()
                        response.body()!!.data.forEach {
                            classIds.add(it.id)
                            classNames.add("${it.name} • ${it.subject}")
                        }
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@CreateTaskActivity, "Gagal load kelas", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showClassPicker() {
        if (classNames.isEmpty()) {
            Toast.makeText(this, "Belum ada kelas", Toast.LENGTH_SHORT).show()
            return
        }
        android.app.AlertDialog.Builder(this)
            .setTitle("Pilih Kelas")
            .setItems(classNames.toTypedArray()) { _, which ->
                selectedClassId = classIds[which]
                tvClass.text = classNames[which]
                tvClass.setTextColor(resources.getColor(android.R.color.black, theme))
            }
            .create().show()
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            showTimePicker(year, month, day)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            val deadline = Calendar.getInstance().apply { set(year, month, day, hour, minute, 0) }
            selectedDeadlineMillis = deadline.timeInMillis
            selectedDeadline = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(deadline.time)
            val display = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")).format(deadline.time)
            tvDeadline.text = display
            tvDeadline.setTextColor(resources.getColor(android.R.color.black, theme))
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun saveTask() {
        val title       = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty()) { Toast.makeText(this, "Judul tugas wajib diisi", Toast.LENGTH_SHORT).show(); return }
        if (description.isEmpty()) { Toast.makeText(this, "Deskripsi wajib diisi", Toast.LENGTH_SHORT).show(); return }
        if (selectedClassId == -1) { Toast.makeText(this, "Pilih kelas terlebih dahulu", Toast.LENGTH_SHORT).show(); return }
        if (selectedDeadline.isEmpty()) { Toast.makeText(this, "Pilih deadline terlebih dahulu", Toast.LENGTH_SHORT).show(); return }

        val classroomIdBody = selectedClassId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val titleBody       = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descBody        = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val deadlineBody    = selectedDeadline.toRequestBody("text/plain".toMediaTypeOrNull())

        // File part (opsional)
        val filePart = selectedFileUri?.let { uri ->
            val file = uriToFile(uri)
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            MultipartBody.Part.createFormData("file", file.name, file.asRequestBody(mimeType.toMediaTypeOrNull()))
        }

        RetrofitClient.getInstance(this).createAssignment(classroomIdBody, titleBody, descBody, deadlineBody, filePart)
            .enqueue(object : Callback<CreateAssignmentResponse> {
                override fun onResponse(call: Call<CreateAssignmentResponse>, response: Response<CreateAssignmentResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val assignmentId = response.body()!!.data?.id ?: 0
                        scheduleDeadlineNotification(assignmentId, title, selectedDeadlineMillis - (24 * 60 * 60 * 1000))
                        Toast.makeText(this@CreateTaskActivity, "Tugas berhasil dibuat!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CreateTaskActivity, response.body()?.message ?: "Gagal membuat tugas", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<CreateAssignmentResponse>, t: Throwable) {
                    Toast.makeText(this@CreateTaskActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun scheduleDeadlineNotification(id: Int, title: String, millis: Long) {
        if (millis <= System.currentTimeMillis()) return
        val intent = Intent(this, DeadlineReceiver::class.java).apply {
            putExtra("id", id)
            putExtra("title", "Deadline Besok!")
            putExtra("message", "Tugas \"$title\" akan berakhir besok!")
        }
        val pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)!!
        val fileName = getFileName(uri)
        val tempFile = File(cacheDir, fileName)
        FileOutputStream(tempFile).use { output -> inputStream.copyTo(output) }
        return tempFile
    }

    private fun getFileName(uri: Uri): String {
        var name = "file_${System.currentTimeMillis()}"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && idx >= 0) name = cursor.getString(idx)
        }
        return name
    }
}