package com.example.schoolreminderr.model

import com.google.gson.annotations.SerializedName

data class ClassroomData(
    val id: Int,
    val name: String,
    val subject: String,
    val description: String?,
    val class_code: String,
    val teacher_id: Int,
    val teacher: User?,
    @SerializedName("class_name") val className: String,
    @SerializedName("teacher_name") val teacherName: String?,
    val students: List<User>?,
    val assignments: List<AssignmentData>?,
    val materials: List<MaterialData>?
)
data class AssignmentData(
    val id: Int,
    val title: String,
    val description: String?,
    val deadline: String?,
    val classroom_id: Int,
    val file: String?,
    val is_submitted: Boolean?
)

data class MaterialData(
    val id: Int,
    val title: String,
    val description: String?,
    val file: String?,
    val classroom_id: Int
)

data class ClassMembersResponse(
    val status: Boolean,
    val teacher: User?,
    val students: List<User>?
)