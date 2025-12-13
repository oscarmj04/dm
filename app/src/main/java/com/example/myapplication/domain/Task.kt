package com.example.myapplication.domain

import com.example.myapplication.Category

data class Task(
    val localId: Int,
    val remoteId: String?,
    var title: String,
    var description: String,
    var dueDate: String,
    var category: Category,
    var done: Boolean
)
