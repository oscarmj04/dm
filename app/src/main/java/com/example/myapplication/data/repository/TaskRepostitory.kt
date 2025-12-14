package com.example.myapplication.data.repository

import androidx.lifecycle.map
import com.example.myapplication.data.local.TaskDao
import com.example.myapplication.data.network.TaskApiService
import com.example.myapplication.domain.Task
import com.example.myapplication.data.mapper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(
    private val dao: TaskDao,
    private val api: TaskApiService
) {

    private var hasRefreshedOnce = false   // ⭐ SOLO refrescar una vez por sesión

    // -----------------------------------------
    // ALWAYS READ FROM ROOM
    // -----------------------------------------
    fun getTasks() =
        dao.getAll().map { list -> list.map { it.toDomain() } }


    // -----------------------------------------
    // REFRESH FROM SERVER → ROOM ONLY ONCE
    // -----------------------------------------
    suspend fun refreshFromRemote() = withContext(Dispatchers.IO) {

        if (hasRefreshedOnce) return@withContext   // ⭐ Enunciado: solo refrescar 1 vez

        hasRefreshedOnce = true

        try {
            val remote = api.getTasks()
            val entities = remote.map { it.toEntity() }
            dao.replaceAll(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // -----------------------------------------
    // ADD TASK (Room-first)
    // -----------------------------------------
    suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {

        // 1) Guardamos primero en ROOM
        val localId = dao.insert(task.toEntity())

        try {
            // 2) Enviamos al servidor (POST)
            val created = api.addTask(task.toDtoForCreate())

            // 3) Vinculamos _id remoto a la entidad local
            dao.updateRemoteId(localId, created.id!!)
        } catch (e: Exception) {
            e.printStackTrace()
            // ⭐ Room-first: mantenemos la tarea en local aunque falle el servidor
        }
    }


    // -----------------------------------------
    // UPDATE TASK (Room-first)
    // -----------------------------------------
    suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {

        // 1) Guardar cambios en ROOM SIEMPRE
        dao.update(task.toEntity())

        // 2) Intentar sincronizar con servidor
        try {
            task.remoteId?.let {
                api.updateTask(it, task.toDtoForUpdate())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // ⭐ Room-first: Error remoto → mantenemos local como fuente de verdad
        }
    }


    // -----------------------------------------
    // DELETE TASK (Room-first)
    // -----------------------------------------
    suspend fun deleteTask(task: Task) = withContext(Dispatchers.IO) {

        // 1) Eliminar SIEMPRE en Room
        dao.delete(task.toEntity())

        // 2) Intentar eliminar en servidor
        try {
            task.remoteId?.let { api.deleteTask(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            // ⭐ Room-first: si falla, se borra igual porque Room es prioridad
        }
    }
}
