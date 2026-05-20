package com.example.schoolreminderr.model

data class LoginResponse(
    val status: Boolean,
    val message: String? = null,
    val token: String? = null,       // nullable
    val user: User? = null           // nullable
)