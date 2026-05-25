package com.example.schoolreminderr.model

data class JoinClassResponse(
    val status: Boolean,
    val message: String
    // Jika server mengembalikan data kelas yang baru diikuti, tambahkan:
    // val data: ClassroomData?
)