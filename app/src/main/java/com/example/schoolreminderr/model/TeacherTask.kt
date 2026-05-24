package com.example.schoolreminderr.model

data class TeacherTask(
    val id: Int,
    val title: String,
    val deadline: String,
    val submission: String,
    val progress: Int,
    val status: String
)