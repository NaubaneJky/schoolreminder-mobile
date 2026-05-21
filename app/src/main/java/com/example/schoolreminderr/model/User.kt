package com.example.schoolreminderr.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val gender: String?,
    val school: String?,
    val photo: String?
)