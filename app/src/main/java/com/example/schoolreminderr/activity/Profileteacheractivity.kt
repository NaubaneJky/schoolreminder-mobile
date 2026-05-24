package com.example.schoolreminderr.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ProfileResponse
import com.example.schoolreminderr.network.RetrofitClient
import com.example.schoolreminderr.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ProfileTeacherActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var imgProfile: ImageView
    private lateinit var btnEdit: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvSchool: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView

    private var selectedPhotoUri: Uri? = null
    private var currentUser: com.example.schoolreminderr.model.User? = null

    private val photoPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedPhotoUri = it
            imgProfile.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_teacher)
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
        sessionManager = SessionManager(this)

        imgProfile = findViewById(R.id.imgProfile)
        btnEdit    = findViewById(R.id.btnEdit)
        tvName     = findViewById(R.id.tvName)
        tvFullName = findViewById(R.id.tvFullName)
        tvGender   = findViewById(R.id.tvGender)
        tvEmail    = findViewById(R.id.tvEmail)
        tvSchool   = findViewById(R.id.tvSchool)
        tvPhone   = findViewById(R.id.tvPhone)
        tvAddress = findViewById(R.id.tvAddress)

        // Klik foto → pilih gambar
        imgProfile.setOnClickListener { photoPicker.launch("image/*") }
        btnEdit.setOnClickListener    { showEditDialog() }

        setupNavbar()
        loadProfile()
    }

    private fun loadProfile() {
        RetrofitClient.getInstance(this).getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!.user
                        currentUser = user

                        tvName.text     = user.name
                        tvFullName.text = user.name
                        tvGender.text   = user.gender ?: "-"
                        tvEmail.text    = user.email ?: "-"
                        tvSchool.text   = user.school ?: "-"
                        tvPhone.text   = user.phone_number ?: "-"
                        tvAddress.text = user.address ?: "-"

                        // Load foto kalau ada
                        if (!user.photo.isNullOrEmpty()) {
                            Glide.with(this@ProfileTeacherActivity)
                                .load("http://192.168.100.6:8000/profiles/${user.photo}")
                                .placeholder(R.drawable.bg_profile_circle)
                                .circleCrop()
                                .into(imgProfile)
                        }
                    } else {
                        Toast.makeText(this@ProfileTeacherActivity, "Gagal load profile", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileTeacherActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showEditDialog() {
        val user = currentUser ?: return

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val etName   = EditText(this).apply { setText(user.name);           hint = "Nama" }
        val etGender = EditText(this).apply { setText(user.gender ?: "");   hint = "Jenis Kelamin (Laki-Laki/Perempuan)" }
        val etSchool = EditText(this).apply { setText(user.school ?: "");   hint = "Nama Sekolah" }
        val etPhone  = EditText(this).apply { setText(user.phone_number ?: ""); hint = "No. HP" }
        val etAddr   = EditText(this).apply { setText(user.address ?: "");  hint = "Alamat" }

        layout.addView(etName)
        layout.addView(etGender)
        layout.addView(etSchool)
        layout.addView(etPhone)
        layout.addView(etAddr)

        android.app.AlertDialog.Builder(this)
            .setTitle("Edit Profil")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                updateProfile(
                    name   = etName.text.toString().trim(),
                    email  = user.email ?: "",
                    gender = etGender.text.toString().trim(),
                    school = etSchool.text.toString().trim(),
                    phone  = etPhone.text.toString().trim(),
                    addr   = etAddr.text.toString().trim()
                )
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updateProfile(name: String, email: String, gender: String, school: String, phone: String, addr: String) {
        val nameBody   = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody  = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toRequestBody("text/plain".toMediaTypeOrNull())
        val schoolBody = school.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneBody  = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val addrBody   = addr.toRequestBody("text/plain".toMediaTypeOrNull())

        val photoPart = selectedPhotoUri?.let { uri ->
            val file     = uriToFile(uri)
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody(mimeType.toMediaTypeOrNull()))
        }

        RetrofitClient.getInstance(this).updateProfile(nameBody, emailBody, genderBody, schoolBody, phoneBody, addrBody, photoPart)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(this@ProfileTeacherActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        selectedPhotoUri = null
                        loadProfile()
                    } else {
                        Toast.makeText(this@ProfileTeacherActivity, "Gagal update profil", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileTeacherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)!!
        val name        = getFileName(uri)
        val tempFile    = File(cacheDir, name)
        FileOutputStream(tempFile).use { it.write(inputStream.readBytes()) }
        return tempFile
    }

    private fun getFileName(uri: Uri): String {
        var name = "photo_${System.currentTimeMillis()}.jpg"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && idx >= 0) name = cursor.getString(idx)
        }
        return name
    }

    private fun setupNavbar() {
        setNavActive(R.id.btnProfile)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            startActivity(Intent(this, HomeTeacherActivity::class.java)); finish()
        }
        findViewById<ImageButton>(R.id.btnClass).setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnNotif).setOnClickListener {
            startActivity(Intent(this, TeacherReminderActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnProfile).setOnClickListener { }
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
