package com.example.schoolreminderr.model

data class ReminderData(
    val id: Int,
    val title: String,
    val classroom: String,
    val submission: String,
    val deadline: String,
    val badge: String
)

