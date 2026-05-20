package com.example.schoolreminderr.model

data class ReminderTask(
    val subject: String,
    val description: String,
    val deadline: String,
    val status: String,
    val color: String
)