package com.example.schoolreminderr.model

data class Submission(
    val id: Int,
    val student_name: String,
    val file: String?,
    val score: Int?
)