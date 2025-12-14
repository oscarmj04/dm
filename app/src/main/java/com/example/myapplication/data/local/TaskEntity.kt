package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,

    val remoteId: String?,
    val title: String,
    val description: String,
    val dueDate: String,
    val category: String,
    val done: Boolean
)
