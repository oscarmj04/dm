package com.example.myapplication.data.network

import com.squareup.moshi.Json

data class TaskDto(
    @Json(name = "_id")
    val id: String?,

    val title: String,
    val description: String,
    val dueDate: String,
    val category: String,
    val done: Boolean
)

