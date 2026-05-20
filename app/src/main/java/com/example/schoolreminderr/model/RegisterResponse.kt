package com.example.schoolreminderr.model

data class RegisterResponse(
    val status: Boolean,
    val message: String? = null,
    val token: String? = null,
    val user: User? = null
)