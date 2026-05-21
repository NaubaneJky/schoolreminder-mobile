package com.example.schoolreminderr.activity

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.CreateMaterialResponse
import com.example.schoolreminderr.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class UploadMaterialActivity : AppCompatActivity() {

    private lateinit var etSubject: EditText
    private lateinit var etDescription: EditText
    private lateinit var tvClass: TextView
    private lateinit var tvFileName: TextView
    private lateinit var btnUpload: LinearLayout
    private lateinit var btnSaveMaterial: Button
    private lateinit var btnBack: ImageView

    private var selectedClassId: Int = -1
    private var selectedFileUri: Uri? = null
    private val classIds = ArrayList<Int>()
    private val classNames = ArrayList<String>()

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedFileUri = it
            tvFileName.text = getFileName(it)
            tvFileName.visibility = TextView.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_material)

        etSubject       = findViewById(R.id.etSubject)
        etDescription   = findViewById(R.id.etDescription)
        tvClass         = findViewById(R.id.tvClass)
        btnSaveMaterial = findViewById(R.id.btnSaveMaterial)
        btnBack         = findViewById(R.id.btnBack)
        btnUpload       = findViewById(R.id.btnUpload)

        tvFileName = TextView(this).apply {
            visibility = TextView.GONE
            setTextColor(resources.getColor(android.R.color.holo_blue_dark, theme))
        }
        btnUpload.addView(tvFileName)

        btnBack.setOnClickListener { finish() }
        tvClass.setOnClickListener { showClassPicker() }
        btnUpload.setOnClickListener { filePicker.launch("*/*") }
        btnSaveMaterial.setOnClickListener { saveMaterial() }

        loadClasses()
    }

    private fun loadClasses() {
        RetrofitClient.getInstance(this).getClasses()
            .enqueue(object : Callback<ClassroomResponse> {
                override fun onResponse(call: Call<ClassroomResponse>, response: Response<ClassroomResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        classIds.clear(); classNames.clear()
                        response.body()!!.data.forEach {
                            classIds.add(it.id)
                            classNames.add("${it.name} • ${it.subject}")
                        }
                    }
                }
                override fun onFailure(call: Call<ClassroomResponse>, t: Throwable) {
                    Toast.makeText(this@UploadMaterialActivity, "Gagal load kelas", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showClassPicker() {
        if (classNames.isEmpty()) { Toast.makeText(this, "Belum ada kelas", Toast.LENGTH_SHORT).show(); return }
        android.app.AlertDialog.Builder(this)
            .setTitle("Pilih Kelas")
            .setItems(classNames.toTypedArray()) { _, which ->
                selectedClassId = classIds[which]
                tvClass.text = classNames[which]
                tvClass.setTextColor(resources.getColor(android.R.color.black, theme))
            }.create().show()
    }

    private fun saveMaterial() {
        val title       = etSubject.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (title.isEmpty()) { Toast.makeText(this, "Nama mata pelajaran wajib diisi", Toast.LENGTH_SHORT).show(); return }
        if (selectedClassId == -1) { Toast.makeText(this, "Pilih kelas terlebih dahulu", Toast.LENGTH_SHORT).show(); return }

        val classroomIdBody = selectedClassId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val titleBody       = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descBody        = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val filePart = selectedFileUri?.let { uri ->
            val file = uriToFile(uri)
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            MultipartBody.Part.createFormData("file", file.name, file.asRequestBody(mimeType.toMediaTypeOrNull()))
        }

        RetrofitClient.getInstance(this).createMaterial(classroomIdBody, titleBody, descBody, filePart)
            .enqueue(object : Callback<CreateMaterialResponse> {
                override fun onResponse(call: Call<CreateMaterialResponse>, response: Response<CreateMaterialResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(this@UploadMaterialActivity, "Materi berhasil diupload!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@UploadMaterialActivity, response.body()?.message ?: "Gagal upload materi", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<CreateMaterialResponse>, t: Throwable) {
                    Toast.makeText(this@UploadMaterialActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
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