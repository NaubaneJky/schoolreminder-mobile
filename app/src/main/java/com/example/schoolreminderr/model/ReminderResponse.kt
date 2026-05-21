package com.example.schoolreminderr.model

data class ReminderResponse(
    val status: Boolean,
    val data: List<ReminderData>
)