package com.example.schoolreminderr.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.TeacherReminderAdapter
import com.example.schoolreminderr.model.ReminderData
import com.example.schoolreminderr.model.ReminderResponse
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TeacherReminderActivity : AppCompatActivity() {

    private lateinit var rvReminder: RecyclerView
    private lateinit var calendarGrid: GridLayout
    private lateinit var tvMonth: TextView
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnPrevMonth: ImageButton
    private lateinit var btnNextMonth: ImageButton

    private var allReminders = ArrayList<ReminderData>()
    private var filteredReminders = ArrayList<ReminderData>()
    private lateinit var adapter: TeacherReminderAdapter

    private var currentCalendar = Calendar.getInstance()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_reminder)

        rvReminder      = findViewById(R.id.rvReminderTeacher)
        calendarGrid    = findViewById(R.id.calendarGrid)
        tvMonth         = findViewById(R.id.tvMonth)
        tvSelectedDate  = findViewById(R.id.tvSelectedDate)
        btnPrevMonth    = findViewById(R.id.btnPrevMonth)
        btnNextMonth    = findViewById(R.id.btnNextMonth)

        adapter = TeacherReminderAdapter(filteredReminders)
        rvReminder.layoutManager = LinearLayoutManager(this)
        rvReminder.adapter = adapter

        // Set selected date ke hari ini
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        btnPrevMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            renderCalendar()
        }

        btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            renderCalendar()
        }

        setupNavbar()
        loadReminder()
    }

    private fun loadReminder() {
        RetrofitClient.getInstance(this).getReminders()
            .enqueue(object : Callback<ReminderResponse> {
                override fun onResponse(call: Call<ReminderResponse>, response: Response<ReminderResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        allReminders.clear()
                        allReminders.addAll(response.body()!!.data)
                        renderCalendar()
                        filterByDate(selectedDate)
                    } else {
                        Toast.makeText(this@TeacherReminderActivity, "Gagal load reminder", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ReminderResponse>, t: Throwable) {
                    Toast.makeText(this@TeacherReminderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun renderCalendar() {
        calendarGrid.removeAllViews()

        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("id"))
        tvMonth.text = monthFormat.format(currentCalendar.time)

        val cal = currentCalendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)

        // Hari pertama bulan (Senin=1, Selasa=2, ... Minggu=7)
        var firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2
        if (firstDayOfWeek < 0) firstDayOfWeek = 6

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val year  = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)

        // Tanggal yang ada tugas
        val taskDates = allReminders.map { it.deadline.substring(0, 10) }.toSet()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Tambah empty cell sebelum hari pertama
        repeat(firstDayOfWeek) {
            calendarGrid.addView(makeEmptyCell())
        }

        // Tambah cell per tanggal
        for (day in 1..daysInMonth) {
            val dateStr = String.format("%04d-%02d-%02d", year, month + 1, day)
            val hasTask = taskDates.contains(dateStr)
            val isToday = dateStr == today
            val isSelected = dateStr == selectedDate

            val cell = makeDateCell(day, hasTask, isToday, isSelected)
            cell.setOnClickListener {
                selectedDate = dateStr
                renderCalendar()
                filterByDate(dateStr)
            }
            calendarGrid.addView(cell)
        }
    }

    private fun makeEmptyCell(): TextView {
        return TextView(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = dpToPx(36)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        }
    }

    private fun makeDateCell(day: Int, hasTask: Boolean, isToday: Boolean, isSelected: Boolean): TextView {
        return TextView(this).apply {
            text = day.toString()
            gravity = Gravity.CENTER
            textSize = 13f
            setTypeface(null, if (isSelected || isToday) Typeface.BOLD else Typeface.NORMAL)

            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = dpToPx(36)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(2, 2, 2, 2)
            }

            when {
                isSelected -> {
                    setBackgroundResource(R.drawable.bg_active_date)
                    setTextColor(Color.WHITE)
                }
                isToday -> {
                    setBackgroundResource(R.drawable.bg_has_task)
                    setTextColor(Color.parseColor("#1D3661"))
                }
                hasTask -> {
                    setTextColor(Color.parseColor("#5FADEB"))
                    setTypeface(null, Typeface.BOLD)
                }
                else -> {
                    setTextColor(Color.parseColor("#444444"))
                }
            }
        }
    }

    private fun filterByDate(dateStr: String) {
        val fmt = SimpleDateFormat("dd MMM yyyy", Locale("id"))
        val cal = Calendar.getInstance()
        try {
            cal.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)!!
            tvSelectedDate.text = "Tugas — ${fmt.format(cal.time)}"
        } catch (e: Exception) {
            tvSelectedDate.text = "Tugas"
        }

        filteredReminders.clear()
        filteredReminders.addAll(allReminders.filter { it.deadline.startsWith(dateStr) })
        adapter.notifyDataSetChanged()

        if (filteredReminders.isEmpty()) {
            tvSelectedDate.text = "${tvSelectedDate.text} (tidak ada tugas)"
        }
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

    private fun setupNavbar() {
        setNavActive(R.id.btnNotif)

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            startActivity(Intent(this, HomeTeacherActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnClass).setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnNotif).setOnClickListener { }
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