package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.Toast
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

class TeacherReminderActivity : AppCompatActivity() {

    private lateinit var rvReminder: RecyclerView

    private var reminderList =
        ArrayList<ReminderData>()

    private lateinit var adapter:
            TeacherReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teacher_reminder)

        rvReminder =
            findViewById(R.id.rvReminderTeacher)

        adapter =
            TeacherReminderAdapter(reminderList)

        rvReminder.layoutManager =
            LinearLayoutManager(this)

        rvReminder.adapter = adapter

        loadReminder()
    }

    private fun loadReminder() {

        RetrofitClient.getInstance(this)
            .getReminders()
            .enqueue(object :
                Callback<ReminderResponse> {

                override fun onResponse(
                    call: Call<ReminderResponse>,
                    response: Response<ReminderResponse>
                ) {

                    if(
                        response.isSuccessful &&
                        response.body() != null
                    ){

                        reminderList.clear()

                        reminderList.addAll(
                            response.body()!!.data
                        )

                        adapter.notifyDataSetChanged()

                    }else {

                        Toast.makeText(
                            this@TeacherReminderActivity,
                            "Error: ${response.code()} \n ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ReminderResponse>,
                    t: Throwable
                ) {

                    t.printStackTrace()

                    Toast.makeText(
                        this@TeacherReminderActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}