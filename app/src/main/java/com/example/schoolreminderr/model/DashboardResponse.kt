package com.example.schoolreminderr.model

data class DashboardResponse(
    val role: String,
    val classes: List<ClassroomData>,
    val assignments: List<AssignmentData>?
)