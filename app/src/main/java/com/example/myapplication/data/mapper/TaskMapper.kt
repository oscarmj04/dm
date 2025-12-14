package com.example.myapplication.data.mapper

import com.example.myapplication.data.local.TaskEntity
import com.example.myapplication.data.network.TaskDto
import com.example.myapplication.domain.Task
import com.example.myapplication.Category

// ----------------------------
// DTO → ENTITY
// ----------------------------
fun TaskDto.toEntity(): TaskEntity =
    TaskEntity(
        localId = 0, // autogen
        remoteId = id,
        title = title,
        description = description,
        dueDate = dueDate,
        category = category,          // String → String (OK)
        done = done
    )

// ----------------------------
// ENTITY → DOMAIN
// ----------------------------
fun TaskEntity.toDomain(): Task =
    Task(
        localId = localId,
        remoteId = remoteId,
        title = title,
        description = description,
        dueDate = dueDate,
        category = Category.valueOf(category),   // String → Enum
        done = done
    )

// ----------------------------
// DOMAIN → ENTITY
// (Guardar en Room)
// ----------------------------
fun Task.toEntity(): TaskEntity =
    TaskEntity(
        localId = localId,
        remoteId = remoteId,
        title = title,
        description = description,
        dueDate = dueDate,
        category = category.name,     // Enum → String
        done = done
    )

// ----------------------------
// DOMAIN → DTO para POST
// ----------------------------
fun Task.toDtoForCreate(): TaskDto =
    TaskDto(
        id = null,
        title = title,
        description = description,
        dueDate = dueDate,
        category = category.name,     // Enum → String
        done = done
    )

// ----------------------------
// DOMAIN → DTO para PUT
// ----------------------------
fun Task.toDtoForUpdate(): TaskDto =
    TaskDto(
        id = null,
        title = title,
        description = description,
        dueDate = dueDate,
        category = category.name,     // Enum → String
        done = done
    )
